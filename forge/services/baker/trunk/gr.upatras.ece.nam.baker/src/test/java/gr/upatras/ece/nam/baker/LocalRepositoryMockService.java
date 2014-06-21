package gr.upatras.ece.nam.baker;

import gr.upatras.ece.nam.baker.model.ServiceMetadata;

import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;


@Path("/localrepotest")
public class LocalRepositoryMockService {

	// just to get an example json!
		@GET
		@Path("/repo/example/{uuid}")
		@Produces("application/json")
		public Response getJsonExampleRepoServiceMetadata(@PathParam("uuid") String uuid) {
			ServiceMetadata sm = new ServiceMetadata(UUID.fromString(uuid) , "test Service");
			sm.setPackageLocation("package.tar.gz");		
			sm.setVersion("1.0.0");
			return Response.ok().entity(sm).build();
		}

}
