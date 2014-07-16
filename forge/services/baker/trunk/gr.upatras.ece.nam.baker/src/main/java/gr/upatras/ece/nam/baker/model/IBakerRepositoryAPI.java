package gr.upatras.ece.nam.baker.model;

import javax.ws.rs.core.Response;

public interface IBakerRepositoryAPI {


	Response getBuns();
	Response getUsers();
	Response getBunMetadataByUUID(String uuid);
	Response downloadBunPackage(String uuid, String bunfile);
	Response uploadBunMetadata(BunMetadata bm);
	
	
}
