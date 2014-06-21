package gr.upatras.ece.nam.baker.model;

import java.nio.file.Path;
import java.util.UUID;

public interface IRepositoryWebClient {
	
	/**
	 * @param uuid
	 * @param url
	 * @return a ServiceMetada object containing info
	 */
	public ServiceMetadata fetchMetadata(UUID uuid, String url);

	
	/**
	 * @param uuid
	 * @param packageLocation
	 * @return the temporary fownload location of file
	 */
	public Path fetchPackageFromLocation(UUID uuid, String packageLocation); 
}
