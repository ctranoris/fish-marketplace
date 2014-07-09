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

package gr.upatras.ece.nam.baker;

import gr.upatras.ece.nam.baker.impl.BakerJpaController;
import gr.upatras.ece.nam.baker.model.IRepositoryWebClient;
import gr.upatras.ece.nam.baker.model.InstalledService;
import gr.upatras.ece.nam.baker.model.InstalledServiceStatus;
import gr.upatras.ece.nam.baker.model.ServiceMetadata;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServiceLifecycleMgmt {

	private static final transient Log logger = LogFactory.getLog(ServiceLifecycleMgmt.class.getName());

	InstalledService installService;
	IRepositoryWebClient repoWebClient;

	BakerJpaController bakerJpaController;
	InstalledServiceStatus targetStatus;

	public ServiceLifecycleMgmt(InstalledService s, IRepositoryWebClient rwc, BakerJpaController jpactr, InstalledServiceStatus ts) {
		installService = s;
		repoWebClient = rwc;
		bakerJpaController = jpactr;
		targetStatus = ts;

		logger.info("ServiceLifecycleMgmt uuid:" + installService.getUuid() + " name:" + installService.getName());
		processState();
	}

	public void processState() {

		logger.info("Task for uuid:" + installService.getUuid() + " is:" + installService.getStatus());

		InstalledServiceStatus entryState = installService.getStatus();

		switch (entryState) {
		case INIT:
			downLoadMetadataInfo();
			break;

		case DOWNLOADING:
			startPackageDownloading();
			break;

		case DOWNLOADED:
			startPackageInstallation();
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
			if (targetStatus == InstalledServiceStatus.STOPPED) {
				installService.setStatus(InstalledServiceStatus.STOPPING);
			} else if (targetStatus == InstalledServiceStatus.UNINSTALLED) {
				installService.setStatus(InstalledServiceStatus.STOPPING);
			}

			break;
		case STOPPING:
			execStoppingPhase();

			break;

		case STOPPED:
			if (targetStatus == InstalledServiceStatus.UNINSTALLED) {
				installService.setStatus(InstalledServiceStatus.UNINSTALLING);
			} else if (targetStatus == InstalledServiceStatus.STARTING) {
				installService.setStatus(InstalledServiceStatus.CONFIGURING);
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

		bakerJpaController.update(installService);

		if ((targetStatus != installService.getStatus()) && (installService.getStatus() != InstalledServiceStatus.FAILED))
			processState();

	}

	private void downLoadMetadataInfo() {
		logger.info("Downloading metadata info...");
		ServiceMetadata smetadata = null;
		if (repoWebClient != null)
			smetadata = repoWebClient.fetchMetadata(installService.getUuid(), installService.getRepoUrl());
		else
			logger.info("repoWebClient == null...FAILED");

		if (smetadata != null) {
			installService.setServiceMetadata(smetadata);
			installService.setStatus(InstalledServiceStatus.DOWNLOADING);
		} else {
			logger.info("smetadata == null...FAILED");
			installService.setStatus(InstalledServiceStatus.FAILED);
		}

	}

	private void startPackageDownloading() {
		logger.info("Downloading installation package: " + installService.getServiceMetadata().getPackageLocation());

		Path destFile = repoWebClient.fetchPackageFromLocation(installService.getUuid(), installService.getServiceMetadata().getPackageLocation());

		if ((destFile != null) && (extractPackage(destFile) == 0)) {
			installService.setStatus(InstalledServiceStatus.DOWNLOADED);
			Path packageLocalPath = destFile.getParent();
			installService.setPackageLocalPath(packageLocalPath.toString());
		} else {
			logger.info("FAILED Downloading installation package: " + installService.getServiceMetadata().getPackageLocation());
			installService.setStatus(InstalledServiceStatus.FAILED);
		}

	}

	public int extractPackage(Path targetPath) {
		String cmdStr = "tar --strip-components=1 -xvzf " + targetPath + " -C " + targetPath.getParent() + "/";
		return executeSystemCommand(cmdStr);
	}

	private void startPackageInstallation() {

		installService.setStatus(InstalledServiceStatus.INSTALLING);
		logger.info("Installing...");

		String cmdStr = installService.getPackageLocalPath() + "/recipes/onInstall";
		logger.info("Will execute recipe 'onInstall' of:" + cmdStr);

		if (executeSystemCommand(cmdStr) == 0) {

			installService.setStatus(InstalledServiceStatus.INSTALLED);
		} else
			installService.setStatus(InstalledServiceStatus.FAILED);

	}

	private void execInstalledPhase() {
		logger.info("execInstalledPhase...");
		String cmdStr = installService.getPackageLocalPath() + "/recipes/onInstallFinish";
		logger.info("Will execute recipe 'onInstallFinish' of:" + cmdStr);

		installService.setInstalledVersion(installService.getServiceMetadata().getVersion());
		installService.setName(installService.getServiceMetadata().getName());
		executeSystemCommand(cmdStr); // we don't care for the exit code
		if (executeSystemCommand(cmdStr) == 0) {
			installService.setStatus(InstalledServiceStatus.CONFIGURING);
		} else
			installService.setStatus(InstalledServiceStatus.FAILED);

	}

	private void execConfiguringPhase() {
		logger.info("execInstalledPhase...");
		String cmdStr = installService.getPackageLocalPath() + "/recipes/onApplyConf";
		logger.info("Will execute recipe 'onApplyConf' of:" + cmdStr);

		executeSystemCommand(cmdStr); // we don't care for the exit code
		if (executeSystemCommand(cmdStr) == 0) {
			installService.setStatus(InstalledServiceStatus.STARTING);
		} else
			installService.setStatus(InstalledServiceStatus.FAILED);

	}

	private void execStartingPhase() {
		logger.info("execStartingPhase...");
		String cmdStr = installService.getPackageLocalPath() + "/recipes/onStart";
		logger.info("Will execute recipe 'onStart' of:" + cmdStr);

		if (executeSystemCommand(cmdStr) == 0) {
			installService.setStatus(InstalledServiceStatus.STARTED);
		} else
			installService.setStatus(InstalledServiceStatus.STOPPED);

	}

	private void execStoppingPhase() {

		logger.info("execStoppingPhase...");
		String cmdStr = installService.getPackageLocalPath() + "/recipes/onStop";
		logger.info("Will execute recipe 'onStop' of:" + cmdStr);

		// if (executeSystemCommand(cmdStr) == 0) {
		// whatever is the return value...it will go to stopped
		installService.setStatus(InstalledServiceStatus.STOPPED);

	}

	private void execUninstallingPhase() {

		logger.info("execUninstallingPhase...");
		String cmdStr = installService.getPackageLocalPath() + "/recipes/onUninstall";
		logger.info("Will execute recipe 'onUninstall' of:" + cmdStr);

		// if (executeSystemCommand(cmdStr) == 0) {
		// whatever is the return value...it will go to stopped
		installService.setStatus(InstalledServiceStatus.UNINSTALLED);

	}

	public int executeSystemCommand(String cmdStr) {

		logger.info(" Execute :" + cmdStr);

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

		} catch (ExecuteException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		logger.info("out>" + out);

		return exitValue;

	}

}
