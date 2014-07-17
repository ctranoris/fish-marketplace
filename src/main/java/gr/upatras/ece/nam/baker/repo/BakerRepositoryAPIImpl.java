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

package gr.upatras.ece.nam.baker.repo;

import gr.upatras.ece.nam.baker.model.BakerUser;
import gr.upatras.ece.nam.baker.model.BunMetadata;
import gr.upatras.ece.nam.baker.model.IBakerRepositoryAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;

@Path("/repo")
public class BakerRepositoryAPIImpl implements IBakerRepositoryAPI {

	@Context
	UriInfo uri;

	private static final transient Log logger = LogFactory.getLog(BakerRepositoryAPIImpl.class.getName());

	private static final String BUNSDATADIR = System.getProperty("user.home") + File.separator +".baker/bunsdata/";

	private BakerRepository bakerRepositoryRef;

	// BakerUser related API

	@GET
	@Path("/users/")
	@Produces("application/json")
	public Response getUsers() {
		return Response.ok().entity(bakerRepositoryRef.getUserValues()).build();
	}

	/**
	 * @return an example user to see how to do POSTS
	 */
	@GET
	@Path("/users/example")
	@Produces("application/json")
	public Response getUserExample() {
		BakerUser b = new BakerUser();
		b.setName("Christos");
		b.setUsername("ctran");
		b.setPassword("passwd");
		b.setOrganization("UoP");
		ResponseBuilder response = Response.ok(b);

		CacheControl cacheControl = new CacheControl();
		cacheControl.setNoCache(true);
		response.cacheControl(cacheControl);

		return response.build();
	}

	@GET
	@Path("/users/{userid}")
	@Produces("application/json")
	public Response getUserById(@PathParam("userid") int userid) {

		BakerUser u = bakerRepositoryRef.getUserByID(userid);

		if (u != null) {
			return Response.ok().entity(u).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("User with id=" + userid + " not found in baker registry");
			throw new WebApplicationException(builder.build());
		}
	}

	@POST
	@Path("/users/")
	@Produces("application/json")
	@Consumes("application/json")
	public Response addUser(BakerUser user) {

		logger.info("Received POST for user: " + user.getUsername());

		BakerUser u = bakerRepositoryRef.addBakerUserToUsers(user);

		if (u != null) {
			return Response.ok().entity(u).build();
		} else {
			ResponseBuilder builder = Response.status(Status.INTERNAL_SERVER_ERROR);
			builder.entity("Requested user with username=" + user.getUsername() + " cannot be installed");
			throw new WebApplicationException(builder.build());
		}
	}

	@PUT
	@Path("/users/{userid}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response updateUserInfo(@PathParam("userid") int userid, BakerUser user) {
		logger.info("Received PUT for user: " + user.getUsername());

		BakerUser u = bakerRepositoryRef.updateUserInfo(userid, user);

		if (u != null) {
			return Response.ok().entity(u).build();
		} else {
			ResponseBuilder builder = Response.status(Status.INTERNAL_SERVER_ERROR);
			builder.entity("Requested user with username=" + user.getUsername() + " cannot be updated");
			throw new WebApplicationException(builder.build());
		}
	}

	@DELETE
	@Path("/users/{userid}")
	@Produces("application/json")
	public Response deleteUser(@PathParam("userid") int userid) {
		logger.info("Received DELETE for userid: " + userid);

		bakerRepositoryRef.deleteUser(userid);

		return Response.ok().build();
	}

	@GET
	@Path("/users/{userid}/buns")
	@Produces("application/json")
	public Response getAllBunsofUser(@PathParam("userid") int userid) {
		logger.info("getAllBunsofUser for userid: " + userid);
		BakerUser u = bakerRepositoryRef.getUserByID(userid);

		if (u != null) {
			List<BunMetadata> buns = u.getBuns();
			// Collection<BunMetadata> b = buns;
			return Response.ok().entity(buns).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("User with id=" + userid + " not found in baker registry");
			throw new WebApplicationException(builder.build());
		}
	}
	
	@GET
	@Path("/users/{userid}/buns/{bunid}")
	@Produces("application/json")
	public Response getBunofUser(@PathParam("userid") int userid, @PathParam("bunid") int bunid) {
		logger.info("getBunofUser for userid: " + userid + ", bunid="+bunid);
		BakerUser u = bakerRepositoryRef.getUserByID(userid);

		if (u != null) {
			BunMetadata bun = u.getBunById(bunid);
			// Collection<BunMetadata> b = buns;
			return Response.ok().entity(bun).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("User with id=" + userid + " not found in baker registry");
			throw new WebApplicationException(builder.build());
		}
	}
	
	

	@POST
	@Path("/users/{userid}/buns/")
	@Consumes("multipart/form-data")
	public void addBunMetadata(@PathParam("userid") int userid, @Multipart(value = "bunname", type = "text/plain") String bunname,
			@Multipart(value = "shortDescription", type = "text/plain") String shortDescription,
			@Multipart(value = "longDescription", type = "text/plain") String longDescription,
			@Multipart(value = "version", type = "text/plain") String version, @Multipart(value = "uploadedBunIcon") Attachment image,
			@Multipart(value = "uploadedBunFile") Attachment bunFile) {

		String imageFileNamePosted = getFileName(image.getHeaders());
		String bunFileNamePosted = getFileName(bunFile.getHeaders());
		logger.info("bunname = " + bunname);
		logger.info("version = " + version);
		logger.info("shortDescription = " + shortDescription);
		logger.info("longDescription = " + longDescription);
		logger.info("image = " + imageFileNamePosted);
		logger.info("bunFile = " + bunFileNamePosted);

		String uuid = UUID.randomUUID().toString();
		BunMetadata sm = new BunMetadata(uuid, bunname);
		sm.setShortDescription(shortDescription);
		sm.setLongDescription(longDescription);
		sm.setVersion(version);

		URI endpointUrl = uri.getBaseUri();

		String tempDir = BUNSDATADIR+uuid+ File.separator;
		try {
			Files.createDirectories( Paths.get( tempDir ) );
			
			if (!imageFileNamePosted.equals("")) {
				String imgfile = saveFile(image, tempDir+imageFileNamePosted);
				logger.info("imgfile saved to = " + imgfile);
				sm.setIconsrc(endpointUrl + "repo/images/" + uuid+ File.separator + imageFileNamePosted);
			}

			if (!bunFileNamePosted.equals("")) {
				String bunfilepath = saveFile(bunFile, tempDir+bunFileNamePosted);
				logger.info("bunfilepath saved to = " + bunfilepath);
				sm.setPackageLocation(endpointUrl + "repo/packages/" + uuid + File.separator + bunFileNamePosted);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		// Save now bun for User
		BakerUser bunOwner = bakerRepositoryRef.getUserByID(userid);
		sm.setOwner(bunOwner);
		bunOwner.addBun(sm);
		bakerRepositoryRef.updateUserInfo(userid, bunOwner);

	}
	
	
	@PUT
	@Path("/users/{userid}/buns/{bid}")
	@Consumes("multipart/form-data")
	public void updateBunMetadata(@PathParam("userid") int userid, @PathParam("bid") int bid, 
			@Multipart(value = "bunname", type = "text/plain") String bunname,
			@Multipart(value = "bunid", type = "text/plain") int bunid,
			@Multipart(value = "bunuuid", type = "text/plain") String uuid,
			@Multipart(value = "shortDescription", type = "text/plain") String shortDescription,
			@Multipart(value = "longDescription", type = "text/plain") String longDescription,
			@Multipart(value = "version", type = "text/plain") String version, @Multipart(value = "uploadedBunIcon") Attachment image,
			@Multipart(value = "uploadedBunFile") Attachment bunFile) {

		String imageFileNamePosted = getFileName(image.getHeaders());
		String bunFileNamePosted = getFileName(bunFile.getHeaders());
		logger.info("bunname = " + bunname);
		logger.info("bunid = " + bunid);;
		logger.info("bunuuid = " + uuid);
		logger.info("version = " + version);
		logger.info("shortDescription = " + shortDescription);
		logger.info("longDescription = " + longDescription);
		logger.info("image = " + imageFileNamePosted);
		logger.info("bunFile = " + bunFileNamePosted);


		// Save now bun for User
		BakerUser bunOwner = bakerRepositoryRef.getUserByID(userid);
		
		BunMetadata sm =bunOwner.getBunById(bunid);
		sm.setShortDescription(shortDescription);
		sm.setLongDescription(longDescription);
		sm.setVersion(version);

		URI endpointUrl = uri.getBaseUri();

		String tempDir = BUNSDATADIR+uuid+ File.separator;
		try {
			Files.createDirectories( Paths.get( tempDir ) );
			
			if (!imageFileNamePosted.equals("")) {
				String imgfile = saveFile(image, tempDir+imageFileNamePosted);
				logger.info("imgfile saved to = " + imgfile);
				sm.setIconsrc(endpointUrl + "repo/images/" + uuid+ File.separator + imageFileNamePosted);
			}

			if (!bunFileNamePosted.equals("")) {
				String bunfilepath = saveFile(bunFile, tempDir+bunFileNamePosted);
				logger.info("bunfilepath saved to = " + bunfilepath);
				sm.setPackageLocation(endpointUrl + "repo/packages/" + uuid + File.separator + bunFileNamePosted);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		bakerRepositoryRef.updateBunInfo(bunid, sm); 

	}

	// Buns related API
	
	@GET
	@Path("/buns")
	@Produces("application/json")
	public Response getBuns() {
		logger.info("getBuns ");
		
		List<BunMetadata> buns = bakerRepositoryRef.getBuns();
		return Response.ok().entity(buns).build();
		
	}
	
	@GET
	@Path("/images/{uuid}/{imgfile}")
    @Produces("image/*")
	public Response getBunImage(@PathParam("uuid") String uuid, @PathParam("imgfile") String imgfile) { 
		logger.info("getBunImage of uuid: " + uuid);
		String imgAbsfile = BUNSDATADIR+uuid+ File.separator+imgfile;
		logger.info("Image RESOURCE FILE: " + imgAbsfile);
		File file = new File(imgAbsfile);
		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition", "attachment; filename=" + file.getName());
		return response.build();
		
	}

	@GET
	@Path("/packages/{uuid}/{bunfile}")
	@Produces("application/gzip")
	public Response downloadBunPackage(@PathParam("uuid") String uuid, @PathParam("bunfile") String bunfile) {

		logger.info("bunfile: " + bunfile);
		logger.info("uuid: " + uuid);

		String bunAbsfile = BUNSDATADIR+uuid+ File.separator+bunfile;
		logger.info("Bun RESOURCE FILE: " + bunAbsfile);
		File file = new File(bunAbsfile);
		
		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition", "attachment; filename=" + file.getName());
		return response.build();
	}

	@GET
	@Path("/ibuns/{uuid}")
	@Produces("application/json")
	public Response getBunMetadataByUUID(@PathParam("uuid") String uuid) {

		logger.info("Received GET for uuid: " + uuid);
		BunMetadata sm = null;

		if (uuid.equals("77777777-668b-4c75-99a9-39b24ed3d8be")) {
			sm = new BunMetadata(uuid, "IntegrTestLocal example service");
			sm.setShortDescription("An example local service");
			sm.setVersion("1.0.0");
			sm.setIconsrc("");
			sm.setLongDescription("");
			URI endpointUrl = uri.getBaseUri();

			sm.setPackageLocation(endpointUrl + "repo/packages/77777777-668b-4c75-99a9-39b24ed3d8be/examplebun.tar.gz");
		}
		if (uuid.equals("12cab8b8-668b-4c75-99a9-39b24ed3d8be")) {
			sm = new BunMetadata(uuid, "AN example service");
			sm.setShortDescription("An example local service");
			sm.setVersion("1.0.0rc1");
			sm.setIconsrc("");
			sm.setLongDescription("");
			URI endpointUrl = uri.getBaseUri();

			sm.setPackageLocation(endpointUrl + "repo/packages/12cab8b8-668b-4c75-99a9-39b24ed3d8be/examplebun.tar.gz");
		} else if (uuid.equals("22cab8b8-668b-4c75-99a9-39b24ed3d8be")) {
			sm = new BunMetadata(uuid, "IntegrTestLocal example ErrInstall service");
			sm.setShortDescription("An example ErrInstall local service");
			sm.setVersion("1.0.0");
			sm.setIconsrc("");
			sm.setLongDescription("");
			URI endpointUrl = uri.getBaseUri();

			sm.setPackageLocation(endpointUrl + "repo/packages/22cab8b8-668b-4c75-99a9-39b24ed3d8be/examplebunErrInstall.tar.gz");
		}

		if (sm != null) {
			return Response.ok().entity(sm).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("Installed bun with uuid=" + uuid + " not found in local registry");
			throw new WebApplicationException(builder.build());
		}

	}



	@Override
	public Response uploadBunMetadata(BunMetadata bm) {
		// TODO Auto-generated method stub
		return null;
	}

	private String saveFile(Attachment att, String filePath) {
		DataHandler handler = att.getDataHandler();
		try {
			InputStream stream = handler.getInputStream();
			MultivaluedMap map = att.getHeaders();
			File f = new File( filePath );
			OutputStream out = new FileOutputStream(f);

			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = stream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			stream.close();
			out.flush();
			out.close();
			return f.getAbsolutePath();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getFileName(MultivaluedMap<String, String> header) {
		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {
				String[] name = filename.split("=");
				String exactFileName = name[1].trim().replaceAll("\"", "");
				return exactFileName;
			}
		}
		return "unknown";
	}

	public BakerRepository getBakerRepositoryRef() {
		return bakerRepositoryRef;
	}

	public void setBakerRepositoryRef(BakerRepository bakerRepositoryRef) {
		this.bakerRepositoryRef = bakerRepositoryRef;
	}
}
