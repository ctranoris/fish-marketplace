/**
 * Copyright 2014 University of Patras 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the License for the specific language governing permissions and limitations under the License.
 */

package gr.upatras.ece.nam.baker.impl;

import gr.upatras.ece.nam.baker.model.IRepositoryWebClient;
import gr.upatras.ece.nam.baker.model.InstalledBun;
import gr.upatras.ece.nam.baker.model.InstalledBunStatus;
import gr.upatras.ece.nam.baker.model.BunMetadata;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class InstalledBunLifecycleMgmt {

	private static final transient Log logger = LogFactory.getLog(InstalledBunLifecycleMgmt.class.getName());

	InstalledBun installedBun;
	IRepositoryWebClient repoWebClient;

	BakerJpaController bakerJpaController;
	private InstalledBunStatus targetStatus;
	private Boolean restartTriggered = false;
	private BunMetadata bunMetadata = null;

	public InstalledBunLifecycleMgmt(InstalledBun b, IRepositoryWebClient rwc, BakerJpaController jpactr, InstalledBunStatus ts) {
		installedBun = b;
		repoWebClient = rwc;
		bakerJpaController = jpactr;
		targetStatus = ts;

		logger.info("ServiceLifecycleMgmt uuid:" + installedBun.getUuid() + " name:" + installedBun.getName());
		processState();
	}

	public void processState() {

		logger.info("Task for uuid:" + installedBun.getUuid() + " is:" + installedBun.getStatus());
		InstalledBunStatus entryState = installedBun.getStatus();
		
		//this is used to restart the service. usefull if reconfiguring.
		if ((targetStatus == InstalledBunStatus.STARTED) &&
				(entryState == InstalledBunStatus.STARTED ) &&
				(!restartTriggered) ){
			logger.info("Entry and Target state are STARTED. A restart will be triggered, with confguration applied.");
			restartTriggered = true;
		}else
			restartTriggered = false;


		switch (entryState) {
		case INIT:
			downLoadMetadataInfo();
			break;

		case DOWNLOADING:
			if (targetStatus == InstalledBunStatus.STOPPED) {
				installedBun.setStatus(InstalledBunStatus.STOPPING);
			} else if (targetStatus == InstalledBunStatus.UNINSTALLED) {
				installedBun.setStatus(InstalledBunStatus.STOPPING);
			}else{
				startPackageDownloading();
			}
			break;

		case DOWNLOADED:
			if (targetStatus == InstalledBunStatus.STOPPED) {
				installedBun.setStatus(InstalledBunStatus.STOPPING);
			} else if (targetStatus == InstalledBunStatus.UNINSTALLED) {
				installedBun.setStatus(InstalledBunStatus.STOPPING);
			}else{
				startPackageInstallation();
					
			}
			break;

		case INSTALLING:

			break;

		case INSTALLED:
			execInstalledPhase();
			break;

		case CONFIGURING:
			execConfiguringPhase();
			break;
		case STARTING:
			execStartingPhase();
			break;
		case STARTED:
			if (targetStatus == InstalledBunStatus.STOPPED) {
				installedBun.setStatus(InstalledBunStatus.STOPPING);
			} else if (targetStatus == InstalledBunStatus.UNINSTALLED) {
				installedBun.setStatus(InstalledBunStatus.STOPPING);
			} else if ( (targetStatus == InstalledBunStatus.STARTED) && restartTriggered) {
				installedBun.setStatus(InstalledBunStatus.STOPPING);
			}

			break;
		case STOPPING:
			execStoppingPhase();

			break;

		case STOPPED:
			if (targetStatus == InstalledBunStatus.UNINSTALLED) {
				installedBun.setStatus(InstalledBunStatus.UNINSTALLING);
			} else if (targetStatus == InstalledBunStatus.STARTED) {
				installedBun.setStatus(InstalledBunStatus.CONFIGURING);
			}
			break;

		case UNINSTALLING:

			execUninstallingPhase();

			break;

		case UNINSTALLED:

			break;

		default:
			break;
		}

		bakerJpaController.updateInstalledBun(installedBun);

		if ((targetStatus != installedBun.getStatus()) && (installedBun.getStatus() != InstalledBunStatus.FAILED))
			processState();

	}

	private void downLoadMetadataInfo() {
		
		logger.info("Downloading metadata info...");
		
		bunMetadata = null;
		if (repoWebClient != null)
			bunMetadata = repoWebClient.fetchMetadata(installedBun.getUuid(), installedBun.getRepoUrl());
		else
			logger.info("repoWebClient == null...FAILED PLEASE CHECK");

		if (bunMetadata != null) {
			installedBun.setStatus(InstalledBunStatus.DOWNLOADING);
		} else {
			logger.info("smetadata == null...FAILED");
			installedBun.setStatus(InstalledBunStatus.FAILED);
		}

	}

	private void startPackageDownloading() {
		logger.info("Downloading installation package: " + bunMetadata.getPackageLocation() );

		Path destFile = repoWebClient.fetchPackageFromLocation(installedBun.getUuid(), bunMetadata.getPackageLocation());

		if ((destFile != null) && (extractPackage(destFile) == 0)) {
			installedBun.setStatus(InstalledBunStatus.DOWNLOADED);
			Path packageLocalPath = destFile.getParent();
			installedBun.setPackageLocalPath(packageLocalPath.toString());
		} else {
			logger.info("FAILED Downloading installation package from: " + bunMetadata.getPackageLocation());
			installedBun.setStatus(InstalledBunStatus.FAILED);
		}

	}

	public int extractPackage(Path targetPath) {
		String cmdStr = "tar --strip-components=1 -xvzf " + targetPath + " -C " + targetPath.getParent() + File.separator;
		return executeSystemCommand(cmdStr);
	}

	private void startPackageInstallation() {

		installedBun.setStatus(InstalledBunStatus.INSTALLING);
		logger.info("Installing...");

		String cmdStr = installedBun.getPackageLocalPath() + "/recipes/onInstall";
		logger.info("Will execute recipe 'onInstall' of:" + cmdStr);

		if (executeSystemCommand(cmdStr) == 0) {

			installedBun.setStatus(InstalledBunStatus.INSTALLED);
		} else
			installedBun.setStatus(InstalledBunStatus.FAILED);

	}

	private void execInstalledPhase() {
		logger.info("execInstalledPhase...");
		String cmdStr = installedBun.getPackageLocalPath() + "/recipes/onInstallFinish";
		logger.info("Will execute recipe 'onInstallFinish' of:" + cmdStr);

		
		executeSystemCommand(cmdStr); // we don't care for the exit code
		if (executeSystemCommand(cmdStr) == 0) {
			installedBun.setStatus(InstalledBunStatus.CONFIGURING);
			installedBun.setName(bunMetadata.getName());
			installedBun.setPackageURL(bunMetadata.getPackageLocation() );
			installedBun.setInstalledVersion(bunMetadata.getVersion());			
		} else
			installedBun.setStatus(InstalledBunStatus.FAILED);

	}

	private void execConfiguringPhase() {
		logger.info("execInstalledPhase...");
		String cmdStr = installedBun.getPackageLocalPath() + "/recipes/onApplyConf";
		logger.info("Will execute recipe 'onApplyConf' of:" + cmdStr);

		executeSystemCommand(cmdStr); // we don't care for the exit code
		if (executeSystemCommand(cmdStr) == 0) {
			installedBun.setStatus(InstalledBunStatus.STARTING);
		} else
			installedBun.setStatus(InstalledBunStatus.FAILED);

	}

	private void execStartingPhase() {
		logger.info("execStartingPhase...");
		String cmdStr = installedBun.getPackageLocalPath() + "/recipes/onStart";
		logger.info("Will execute recipe 'onStart' of:" + cmdStr);

		if (executeSystemCommand(cmdStr) == 0) {
			installedBun.setStatus(InstalledBunStatus.STARTED);
		} else
			installedBun.setStatus(InstalledBunStatus.STOPPED);

	}

	private void execStoppingPhase() {

		logger.info("execStoppingPhase...");
		String cmdStr = installedBun.getPackageLocalPath() + "/recipes/onStop";
		logger.info("Will execute recipe 'onStop' of:" + cmdStr);

		// if (executeSystemCommand(cmdStr) == 0) {
		// whatever is the return value...it will go to stopped
		executeSystemCommand(cmdStr);
		installedBun.setStatus(InstalledBunStatus.STOPPED);

	}

	private void execUninstallingPhase() {

		logger.info("execUninstallingPhase...");
		String cmdStr = installedBun.getPackageLocalPath() + "/recipes/onUninstall";
		logger.info("Will execute recipe 'onUninstall' of:" + cmdStr);

		// if (executeSystemCommand(cmdStr) == 0) {
		// whatever is the return value...it will go to stopped
		executeSystemCommand(cmdStr);
		installedBun.setStatus(InstalledBunStatus.UNINSTALLED);

	}

	public int executeSystemCommand(String cmdStr) {

		logger.info(" ================> Execute :" + cmdStr);

		CommandLine cmdLine = CommandLine.parse(cmdStr);
		final Executor executor = new DefaultExecutor();
		// create the executor and consider the exitValue '0' as success
		executor.setExitValue(0);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PumpStreamHandler streamHandler = new PumpStreamHandler(out);
		executor.setStreamHandler(streamHandler);

		int exitValue = -1;
		try {
			exitValue = executor.execute(cmdLine);
			logger.info(" ================> EXIT ("+exitValue+") FROM :" + cmdStr);

		} catch (ExecuteException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		logger.info("out>" + out);

		return exitValue;

	}

}
