package gr.upatras.ece.nam.baker.model;

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
	public String fetchPackageFromLocation(UUID uuid, String packageLocation);

}
