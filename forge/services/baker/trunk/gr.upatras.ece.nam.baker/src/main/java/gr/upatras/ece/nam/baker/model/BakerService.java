package gr.upatras.ece.nam.baker.model;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BakerService {
	
	private ConcurrentHashMap< UUID , InstalledService> managedServices;	
	private static final transient Log logger = LogFactory.getLog(BakerService.class.getName());

	public BakerService() {
		managedServices = new ConcurrentHashMap<>();
	}

	public ConcurrentHashMap< UUID , InstalledService> getManagedServices() {
		return managedServices;
	}

	private InstalledService addServiceToManagedServices(InstalledService s){
		managedServices.put(s.getUuid(), s);
		return s;
	}
	
	private Boolean removeServiceFromManagedServices(InstalledService s){
		InstalledService is = managedServices.remove(s.getUuid());
		return (is != null);
	}
	
	public InstalledService installService(UUID uuid, String repourl, String version){
		String serviceName = "NameMustComeFromRepo";
		InstalledService s =new InstalledService(uuid, serviceName, repourl, version);
		addServiceToManagedServices(s);
		return s;
	}
	
	public Boolean uninstallService(UUID uuid){
		InstalledService is = getService(uuid);
		Boolean res = removeServiceFromManagedServices(is);
		return res;
	}
	

	public InstalledService getService(UUID uuid){
		InstalledService is = managedServices.get(uuid); 
		return is;
	}
	
	
	
}
