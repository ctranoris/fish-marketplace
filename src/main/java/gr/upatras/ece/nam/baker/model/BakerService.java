package gr.upatras.ece.nam.baker.model;

import gr.upatras.ece.nam.baker.InstallationTask;
import gr.upatras.ece.nam.baker.impl.RepositoryWebClient;
import gr.upatras.ece.nam.baker.testclasses.MockRepositoryWebClient;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BakerService {

	private ConcurrentHashMap<UUID, InstalledService> managedServices;
	private IRepositoryWebClient repoWebClient;
	
	private static final transient Log logger = LogFactory.getLog(BakerService.class.getName());

	public BakerService() {
		managedServices = new ConcurrentHashMap<>();
		this.setRepoWebClient( new RepositoryWebClient() );
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

	public InstalledService installService(UUID uuid, String repourl) {
		
		InstalledService s = managedServices.get(uuid); //return existing if found
		
		if (s == null) {
			s = new InstalledService(uuid, repourl);			
			addServiceToManagedServices(s);
			handleInstallationJob(s);
		}
		return s;
	}

	private void handleInstallationJob(InstalledService s) {
		Runnable run = new InstallationTask(s, repoWebClient);
		Thread thread = new Thread(run);
		thread.start();

	}

	

	public Boolean uninstallService(UUID uuid) {
		InstalledService is = getService(uuid);
		Boolean res = removeServiceFromManagedServices(is);
		return res;
	}

	
	public InstalledService getService(UUID uuid) {
		InstalledService is = managedServices.get(uuid);
		return is;
	}

	public IRepositoryWebClient getRepoWebClient() {
		return repoWebClient;
	}

	public void setRepoWebClient(IRepositoryWebClient repoWebClient) {
		this.repoWebClient = repoWebClient;
	}

}
