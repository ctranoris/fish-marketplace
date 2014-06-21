package gr.upatras.ece.nam.baker;

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

	public InstallationTask(InstalledService s, IRepositoryWebClient rwc) {
		installService = s;
		repoWebClient = rwc;

		logger.info("new InstallationTask started for uuid:"
				+ installService.getUuid() + " name:"
				+ installService.getName());
	}

	@Override
	public void run() {

		while ((installService.getStatus() != InstalledServiceStatus.INSTALLED)
				&& (installService.getStatus() != InstalledServiceStatus.FAILED)) {
			logger.info("task for uuid:" + installService.getUuid() + " from:"
					+ installService.getStatus());

			switch (installService.getStatus()) {

			case INIT:
				logger.info("Downloading metadata info...");

				ServiceMetadata smetadata = null;
				if (repoWebClient != null)
					smetadata = repoWebClient.fetchMetadata(
							installService.getUuid(),
							installService.getRepoUrl());

				if (smetadata != null) {
					installService.setServiceMetadata(smetadata);
					installService
							.setStatus(InstalledServiceStatus.DOWNLOADING);
				} else {
					installService.setStatus(InstalledServiceStatus.FAILED);
				}

				break;

			case DOWNLOADING:
				startPackageDownloading();
				break;

			case DOWNLOADED:
				startPackageInstallation();
				break;

			case INSTALLING:

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

	private void startPackageDownloading() {
		logger.info("Downloading installation package: "
				+ installService.getServiceMetadata().getPackageLocation());

		String filename = repoWebClient.fetchPackageFromLocation(installService
				.getUuid(), installService.getServiceMetadata()
				.getPackageLocation());

		if (filename != null)
			installService.setStatus(InstalledServiceStatus.DOWNLOADED);
		else
			installService.setStatus(InstalledServiceStatus.FAILED);

	}

	private void startPackageInstallation() {

		installService.setStatus(InstalledServiceStatus.INSTALLING);
		logger.info("Installing...");

		// if installation was OK
		installService.setInstalledVersion(installService.getServiceMetadata()
				.getVersion());
		installService.setName(installService.getServiceMetadata().getName());
		installService.setStatus(InstalledServiceStatus.INSTALLED);

	}

}
