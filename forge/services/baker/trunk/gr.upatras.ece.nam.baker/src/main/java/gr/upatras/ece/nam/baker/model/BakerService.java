package gr.upatras.ece.nam.baker.model;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BakerService {

	private ConcurrentHashMap<UUID, InstalledService> managedServices;
	private static final transient Log logger = LogFactory
			.getLog(BakerService.class.getName());

	public BakerService() {
		managedServices = new ConcurrentHashMap<>();
	}

	public ConcurrentHashMap<UUID, InstalledService> getManagedServices() {
		return managedServices;
	}

	private InstalledService addServiceToManagedServices(InstalledService s) {
		managedServices.put(s.getUuid(), s);
		return s;
	}

	private Boolean removeServiceFromManagedServices(InstalledService s) {
		InstalledService is = managedServices.remove(s.getUuid());
		return (is != null);
	}

	public InstalledService installService(UUID uuid, String repourl,
			String version) {
		InstalledService s = managedServices.get(uuid);
		if (s == null) {
			String serviceName = "(pending)";
			s = new InstalledService(uuid, serviceName, repourl, version);
			addServiceToManagedServices(s);
			handleInstallationJob(s);
		}
		return s;
	}

	private void handleInstallationJob(InstalledService s) {
		Runnable run = new InstallationTask(s);
		Thread thread = new Thread(run);
		thread.start();

	}

	class InstallationTask implements Runnable {

		InstalledService installService;

		InstallationTask(InstalledService s) {
			installService = s;
			logger.info("new InstallationTask started for uuid:"
					+ installService.getUuid() + " name:"
					+ installService.getName());
		}

		@Override
		public void run() {

			try {

				
				while ( (installService.getStatus() != InstalledServiceStatus.INSTALLED) &&
						(installService.getStatus() != InstalledServiceStatus.FAILED)) {
					logger.info("task for uuid:"
							+ installService.getUuid() + " from:"+ installService.getStatus());
					
					switch (installService.getStatus()) {
					
					case INIT:			
						logger.info("Downloading metadata info...");
						Thread.sleep(5000);
						installService.setStatus( InstalledServiceStatus.DOWNLOADING );
						startDownloading();
						break;

					case DOWNLOADING:						
						installService.setStatus( InstalledServiceStatus.DOWNLOADED );
						break;
						
					case DOWNLOADED:						
						installService.setStatus( InstalledServiceStatus.INSTALLING );
						break;

					case INSTALLING:						
						installService.setStatus( InstalledServiceStatus.INSTALLED );
						break;


					default:
						break;
					}
					

					Thread.sleep(5000);
					
				}
				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public Boolean uninstallService(UUID uuid) {
		InstalledService is = getService(uuid);
		Boolean res = removeServiceFromManagedServices(is);
		return res;
	}

	private void startDownloading() {
		logger.info("Downloading...");
		
	}

	public InstalledService getService(UUID uuid) {
		InstalledService is = managedServices.get(uuid);
		return is;
	}

}
