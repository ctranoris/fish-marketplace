/**
 * Copyright 2014 University of Patras 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the License for the specific language governing permissions and limitations under the License.
 */

package gr.upatras.ece.nam.baker.impl;

import java.net.URI;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import gr.upatras.ece.nam.baker.model.IBakerClientAPI;
import gr.upatras.ece.nam.baker.model.InstalledBun;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

@Path("/client")
public class BakerClientAPIImpl implements IBakerClientAPI {
	private static final transient Log logger = LogFactory.getLog(BakerClientAPIImpl.class.getName());

	
	
//	Add everywhere a Header showing a version
//	X-Baker-API-Version : 1.0.0
	
	
	@Context
	UriInfo uri;
	//see more about COntext at example http://www.blackpepper.co.uk/custom-context-providers-for-cxf-with-the-context-annotation/

	private BakerInstallationMgmt bakerInstallationMgmtRef;

	public BakerInstallationMgmt getBakerInstallationMgmtRef() {
		return bakerInstallationMgmtRef;
	}

	public void setBakerInstallationMgmtRef(BakerInstallationMgmt bakerServiceRef) {
		this.bakerInstallationMgmtRef = bakerServiceRef;
	}

	// just to get an example json!
	@GET
	@Path("/ibuns/example")
	@Produces("application/json")
	public Response getJsonInstalledBunExample(@Context HttpHeaders headers, @Context  HttpServletRequest request) {
		
		
		String userAgent = headers.getRequestHeader("user-agent").get(0);
		logger.info("Received GET for Example. user-agent= " + userAgent);
		
		if ( headers.getRequestHeaders().get("X-Baker-API-Version") != null ){
			String XBakerAPIVersion = headers.getRequestHeader("X-Baker-API-Version").get(0);
			logger.info("Received GET for Example. X-Baker-API-Version= " + XBakerAPIVersion);
		}
		
		Map<String, Cookie> cookies = headers.getCookies();		
		logger.info("cookies for Example = " + cookies.toString() );
		HttpSession  session = request.getSession(true);
		logger.info("session = " + session.getId());

		URI endpointUrl = uri.getBaseUri();

		InstalledBun installedBun = new InstalledBun(("12cab8b8-668b-4c75-99a9-39b24ed3d8be"), endpointUrl
				+ "repo/ibuns/12cab8b8-668b-4c75-99a9-39b24ed3d8be");
		installedBun.setName("ServiceName");

		ResponseBuilder response = Response.ok(installedBun);
		
		CacheControl cacheControl = new CacheControl();
		cacheControl.setNoCache(true);
		response.cacheControl(cacheControl);

		return response.build();
	}

	@GET
	@Path("/ibuns/{uuid}")
	@Produces("application/json")
	public Response getInstalledBunInfoByUUID(@PathParam("uuid") String uuid) {

		logger.info("Received GET for uuid: " + uuid);

		InstalledBun installedBun = bakerInstallationMgmtRef.getBun(uuid);

		if (installedBun != null) {
			return Response.ok().entity(installedBun).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("Installed bun with uuid=" + uuid + " not found in baker client registry");
			throw new WebApplicationException(builder.build());
		}

	}

	@GET
	@Path("/ibuns/")
	@Produces("application/json")
	public Response getInstalledBuns() {

		// for (int i = 0; i < 20; i++) { //add 20 more random
		// bakerServiceRef.installService( UUID.randomUUID() ,
		// "www.repoexample.comRANDOM", "1.1.1RANDOM"+i);
		// }
		return Response.ok().entity(bakerInstallationMgmtRef.getManagedInstalledBuns().values()).build();

	}

	@POST
	@Path("/ibuns/")
	@Produces("application/json")
	public Response installBun(InstalledBun reqInstallBun) {

		logger.info("Received POST for uuid: " + reqInstallBun.getUuid());

		InstalledBun installedBun = bakerInstallationMgmtRef.installBunAndStart(reqInstallBun.getUuid(), reqInstallBun.getRepoUrl());

		if (installedBun != null) {
			return Response.ok().entity(installedBun).build();
		} else {
			ResponseBuilder builder = Response.status(Status.INTERNAL_SERVER_ERROR);
			builder.entity("Requested Bun with uuid=" + reqInstallBun.getUuid() + " cannot be installed");
			throw new WebApplicationException(builder.build());
		}

	}
	
	

	@DELETE
	@Path("/ibuns/{uuid}")
	@Produces("application/json")
	public Response uninstallBun(@PathParam("uuid") String uuid) {

		logger.info("Received @DELETE for uuid: " + uuid);

		InstalledBun installedBun = bakerInstallationMgmtRef.getBun(uuid);

		if (installedBun != null) {
			bakerInstallationMgmtRef.uninstallBun(uuid);
			return Response.ok().entity(installedBun).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("Installed bun with uuid=" + uuid + " not found in baker client registry");
			throw new WebApplicationException(builder.build());
		}

	}

	@PUT
	@Path("/ibuns/{uuid}/stop")
	@Produces("application/json")
	public Response stopBun(@PathParam("uuid") String uuid) {

		logger.info("Received @PUT (stop) for uuid: " + uuid);

		InstalledBun installedBun = bakerInstallationMgmtRef.getBun(uuid);

		if (installedBun != null) {
			bakerInstallationMgmtRef.stopBun(uuid);
			return Response.ok().entity(installedBun).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("Installed bun with uuid=" + uuid + " not found in baker client registry");
			throw new WebApplicationException(builder.build());
		}
	}

	@PUT
	@Path("/ibuns/{uuid}/start")
	@Produces("application/json")
	public Response startBun(@PathParam("uuid") String uuid) {

		logger.info("Received  @PUT (start) for uuid: " + uuid);

		InstalledBun installedBun = bakerInstallationMgmtRef.getBun(uuid);

		if (installedBun != null) {
			bakerInstallationMgmtRef.startBun(uuid);
			return Response.ok().entity(installedBun).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("Installed bun with uuid=" + uuid + " not found in baker client registry");
			throw new WebApplicationException(builder.build());
		}
	}

	@PUT
	@Path("/ibuns/{uuid}/reconfigure")
	@Produces("application/json")
	public Response reConfigureBun(String uuid) {
		logger.info("Received  @PUT (reconfigure) for uuid: " + uuid);

		InstalledBun installedBun = bakerInstallationMgmtRef.getBun(uuid);

		if (installedBun != null) {
			bakerInstallationMgmtRef.configureBun(uuid);
			return Response.ok().entity(installedBun).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("Installed bun with uuid=" + uuid + " not found in baker client registry");
			throw new WebApplicationException(builder.build());
		}
	}
	
	
	
	/****************FIREAdapters configure*/
	
	@GET
	@Path("/fireadapters/")
	@Produces("application/json")
	public Response getInstalledFIREAdapters() {

		return getInstalledBuns();

	}
	
	@GET
	@Path("/fireadapters/{uuid}")
	@Produces("application/json")
	public Response getInstalledFIREAdapterInfoByUUID(@PathParam("uuid") String uuid) {
		return getInstalledBunInfoByUUID(uuid);
	}

	

	@POST
	@Path("/fireadapters/")
	@Produces("application/json")
	public Response installfireadapter(InstalledBun reqInstallBun) {
		return installBun(reqInstallBun);
	}
	
	

	@DELETE
	@Path("/fireadapters/{uuid}")
	@Produces("application/json")
	public Response uninstallFireadapter(@PathParam("uuid") String uuid) {
		
		return uninstallBun(uuid);
	}

	@PUT
	@Path("/fireadapters/{uuid}/stop")
	@Produces("application/json")
	public Response stopfireadapter(@PathParam("uuid") String uuid) {
		return stopBun(uuid);
	}

	@PUT
	@Path("/fireadapters/{uuid}/start")
	@Produces("application/json")
	public Response startFireadapter(@PathParam("uuid") String uuid) {
		return startBun(uuid);
	
	}

	@PUT
	@Path("/fireadapters/{uuid}/reconfigure")
	@Produces("application/json")
	public Response reConfigureFireadapter(String uuid) {
		return reConfigureBun(uuid);
	}


}
