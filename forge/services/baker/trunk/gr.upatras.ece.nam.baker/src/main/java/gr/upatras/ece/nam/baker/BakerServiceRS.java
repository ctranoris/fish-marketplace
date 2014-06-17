package gr.upatras.ece.nam.baker;

import java.util.UUID;

import gr.upatras.ece.nam.baker.model.BakerService;
import gr.upatras.ece.nam.baker.model.InstalledService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

@Path("/baker")
public class BakerServiceRS {
	private static final transient Log logger = LogFactory
			.getLog(BakerServiceRS.class.getName());


	private BakerService bakerServiceRef ;
	
	
	public BakerService getBakerServiceRef() {
		return bakerServiceRef;
	}

	public void setBakerServiceRef(BakerService bakerServiceRef) {
		this.bakerServiceRef = bakerServiceRef;
	}

	// just to get an example json!
	@GET
	@Path("/iservices/example")
	@Produces("application/json")
	public Response getJsonInstalledServiceExample() {
		InstalledService installedService = new InstalledService(
				UUID.randomUUID(), "ServiceName",
				"www.ExampleRepoUrl.com/example", "1.0.1 rc1");
		return Response.ok().entity(installedService).build();
	}

	@GET
	@Path("/iservices/{uuid}")
	@Produces("application/json")
	public Response getJsonInstalledService(@PathParam("uuid") String uuid) {

		logger.info("Received GET for uuid: "+uuid);
		
		InstalledService installedService = bakerServiceRef.getService(UUID.fromString(uuid));

		if (installedService != null) {
			return Response.ok().entity(installedService).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("Installed service with uuid=" + uuid
					+ " not found in baker registry");
			throw new WebApplicationException(builder.build());
		}

	}
	
	@GET
	@Path("/iservices/")
	@Produces("application/json")
	public Response getJsonInstalledServices() {

		//for (int i = 0; i < 20; i++) { //add 20 more random
		//	bakerServiceRef.installService( UUID.randomUUID() ,  "www.repoexample.comRANDOM", "1.1.1RANDOM"+i);
		//}		
		return Response.ok().entity(bakerServiceRef.getManagedServices().values()).build();
		
	}

	@POST
	@Path("/iservices/")
	@Produces("application/json")
	public Response jsonInstallService(InstalledService reqInstallService) {

		logger.info("Received POST for uuid: "+ reqInstallService.getUuid());

		InstalledService installedService = bakerServiceRef.installService(
				reqInstallService.getUuid(), reqInstallService.getRepoUrl(),
				reqInstallService.getInstalledVersion());

		if (installedService != null) {
			return Response.ok().entity(installedService).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("Requested Service with uuid="
					+ reqInstallService.getUuid() + " cannot be installed");
			throw new WebApplicationException(builder.build());
		}

	}

}
