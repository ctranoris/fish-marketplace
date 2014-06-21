package gr.upatras.ece.nam.baker;

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

import gr.upatras.ece.nam.baker.model.BakerService;
import gr.upatras.ece.nam.baker.model.IRepositoryWebClient;
import gr.upatras.ece.nam.baker.model.InstalledService;
import gr.upatras.ece.nam.baker.model.InstalledServiceStatus;
import gr.upatras.ece.nam.baker.model.ServiceMetadata;

public class InstallationTask implements Runnable {

	private static final transient Log logger = LogFactory
			.getLog(InstallationTask.class.getName());

	InstalledService installService;
	IRepositoryWebClient repoWebClient;

	private Path packageLocalPath;

	public InstallationTask(InstalledService s, IRepositoryWebClient rwc) {
		installService = s;
		repoWebClient = rwc;

		logger.info("new InstallationTask started for uuid:"
				+ installService.getUuid() + " name:"
				+ installService.getName());
	}

	@Override
	public void run() {

		while (
				(installService.getStatus() != InstalledServiceStatus.STARTED) && 
				(installService.getStatus() != InstalledServiceStatus.FAILED)) {
			logger.info("task for uuid:" + installService.getUuid() + " is:"+ installService.getStatus());

			switch (installService.getStatus()) {

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
			default:
				break;
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private void downLoadMetadataInfo() {
		logger.info("Downloading metadata info...");
		ServiceMetadata smetadata = null;
		if (repoWebClient != null)
			smetadata = repoWebClient.fetchMetadata(installService.getUuid(),
					installService.getRepoUrl());

		if (smetadata != null) {
			installService.setServiceMetadata(smetadata);
			installService.setStatus(InstalledServiceStatus.DOWNLOADING);
		} else {
			installService.setStatus(InstalledServiceStatus.FAILED);
		}

	}

	private void startPackageDownloading() {
		logger.info("Downloading installation package: "
				+ installService.getServiceMetadata().getPackageLocation());

		Path destFile = repoWebClient.fetchPackageFromLocation(installService
				.getUuid(), installService.getServiceMetadata()
				.getPackageLocation());

		if ((destFile != null) && (extractPackage(destFile) == 0)) {
			installService.setStatus(InstalledServiceStatus.DOWNLOADED);
			packageLocalPath = destFile.getParent();
		} else
			installService.setStatus(InstalledServiceStatus.FAILED);

	}

	private void startPackageInstallation() {

		installService.setStatus(InstalledServiceStatus.INSTALLING);
		logger.info("Installing...");

		String cmdStr = packageLocalPath + "/recipes/onInstall";
		logger.info("Will execute recipe 'onInstall' of:" + cmdStr);

		if (executeSystemCommand(cmdStr) == 0) {

			installService.setStatus(InstalledServiceStatus.INSTALLED);
		} else
			installService.setStatus(InstalledServiceStatus.FAILED);

	}

	private void execInstalledPhase() {
		logger.info("execInstalledPhase...");
		String cmdStr = packageLocalPath + "/recipes/onInstallFinish";
		logger.info("Will execute recipe 'onInstallFinish' of:" + cmdStr);
		
		installService.setInstalledVersion(installService.getServiceMetadata().getVersion());
		installService.setName(installService.getServiceMetadata().getName());
		executeSystemCommand(cmdStr); //we don't care for the exit code
		installService.setStatus(InstalledServiceStatus.CONFIGURING);

	}
	
	private void execConfiguringPhase(){
		logger.info("execInstalledPhase...");
		String cmdStr = packageLocalPath + "/recipes/onApplyConf";
		logger.info("Will execute recipe 'onApplyConf' of:" + cmdStr);

		executeSystemCommand(cmdStr); //we don't care for the exit code
		installService.setStatus(InstalledServiceStatus.STARTING);
		
	}
	
	private void execStartingPhase(){
		logger.info("execStartingPhase...");
		String cmdStr = packageLocalPath + "/recipes/onStart";
		logger.info("Will execute recipe 'onStart' of:" + cmdStr);
		
		if (executeSystemCommand(cmdStr) == 0) {			
			installService.setStatus(InstalledServiceStatus.STARTED);
		}else
			installService.setStatus(InstalledServiceStatus.STOPPED);
		
	}
	
	
	

	public int extractPackage(Path targetPath) {
		String cmdStr = "tar --strip-components=1 -xvzf " + targetPath + " -C "
				+ targetPath.getParent() + "/";
		return executeSystemCommand(cmdStr);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("out>" + out);

		return exitValue;

	}

}
