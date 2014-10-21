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

import gr.upatras.ece.nam.baker.model.ApplicationMetadata;
import gr.upatras.ece.nam.baker.model.BakerUser;
import gr.upatras.ece.nam.baker.model.BunMetadata;
import gr.upatras.ece.nam.baker.model.Category;
import gr.upatras.ece.nam.baker.model.Course;
import gr.upatras.ece.nam.baker.model.IBakerRepositoryAPI;
import gr.upatras.ece.nam.baker.model.Product;
import gr.upatras.ece.nam.baker.model.SubscribedMachine;
import gr.upatras.ece.nam.baker.model.UserSession;
import gr.upatras.ece.nam.baker.model.Widget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.cxf.rs.security.cors.CorsHeaderConstants;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing;
import org.apache.cxf.rs.security.cors.LocalPreflight;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

//CORS support
//@CrossOriginResourceSharing(
//        allowOrigins = {
//           "http://83.212.106.218"
//        },
//        allowCredentials = true
//        
//)
@Path("/repo")
public class BakerRepositoryAPIImpl implements IBakerRepositoryAPI {

	@Context
	UriInfo uri;
	
	@Context
	HttpHeaders headers;

	@Context
	MessageContext ws;

	@Context
	protected SecurityContext securityContext;

	private static final transient Log logger = LogFactory.getLog(BakerRepositoryAPIImpl.class.getName());

	private static final String METADATADIR = System.getProperty("user.home") + File.separator + ".baker/metadata/";

	private BakerRepository bakerRepositoryRef;

	// BakerUser related API

	@GET
	@Path("/users/")
	@Produces("application/json")
	// @RolesAllowed("admin") //see this for this annotation
	// http://pic.dhe.ibm.com/infocenter/radhelp/v9/index.jsp?topic=%2Fcom.ibm.javaee.doc%2Ftopics%2Ftsecuringejee.html
	public Response getUsers() {

		if (securityContext != null) {
			if (securityContext.getUserPrincipal() != null)
				logger.info(" securityContext.getUserPrincipal().toString() >" + securityContext.getUserPrincipal().getName() + "<");

		}

		return Response.ok().entity(bakerRepositoryRef.getUserValues()).build();
	}

	/**
	 * @return an example user to see how to do POSTS
	 */
	@GET
	@Path("/users/example")
	@Produces("application/json")
	public Response getUserExample() {

		if (securityContext != null) {
			if (securityContext.getUserPrincipal() != null)
				logger.info(" securityContext.getUserPrincipal().toString() >" + securityContext.getUserPrincipal().toString() + "<");

		}

		Subject currentUser = SecurityUtils.getSubject();
		if (currentUser != null) {
			logger.info(" currentUser = " + currentUser.toString());
			logger.info("User [" + currentUser.getPrincipal() + "] logged in successfully.");
			logger.info(" currentUser  employee  = " + currentUser.hasRole("employee"));
			logger.info(" currentUser  boss  = " + currentUser.hasRole("boss"));
		}

		if (ws != null) {
			logger.info("ws = " + ws.toString());
			if (ws.getHttpServletRequest() != null) {
				// sessionid
				logger.info("ws.getHttpServletRequest() = " + ws.getHttpServletRequest().getSession().getId());

				if (ws.getHttpServletRequest().getUserPrincipal() != null)
					logger.info(" ws.getUserPrincipal().toString(): " + ws.getHttpServletRequest().getUserPrincipal().toString());

			}
		}

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

		logger.info("Received POST for usergetName: " + user.getName());
		logger.info("Received POST for usergetUsername: " + user.getUsername());
		// logger.info("Received POST for usergetPassword: " + user.getPassword());
		// logger.info("Received POST for usergetOrganization: " + user.getOrganization());

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

		BakerUser previousUser = bakerRepositoryRef.getUserByID(userid);
		

		List<Product> previousProducts = previousUser.getProducts();

		if (user.getProducts().size() == 0) {
			user.getProducts().addAll(previousProducts);
		}

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
			List<Product> prods = u.getProducts();
			List<BunMetadata> buns = new ArrayList<BunMetadata>();
			for (Product p : prods) {
				if (p instanceof BunMetadata)
					buns.add(  (BunMetadata) p );
			}

			return Response.ok().entity(buns).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("User with id=" + userid + " not found in baker registry");
			throw new WebApplicationException(builder.build());
		}
	}
	
	
	@GET
	@Path("/users/{userid}/apps")
	@Produces("application/json")
	public Response getAllAppsofUser(@PathParam("userid") int userid) {
		logger.info("getAllAppsofUser for userid: " + userid);
		BakerUser u = bakerRepositoryRef.getUserByID(userid);

		if (u != null) {
			List<Product> prods = u.getProducts();
			List<ApplicationMetadata> apps = new ArrayList<ApplicationMetadata>();
			for (Product p : prods) {
				if (p instanceof ApplicationMetadata)
					apps.add(  (ApplicationMetadata) p );
			}

			return Response.ok().entity(apps).build();
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
		logger.info("getBunofUser for userid: " + userid + ", bunid=" + bunid);
		BakerUser u = bakerRepositoryRef.getUserByID(userid);

		if (u != null) {
			BunMetadata bun = (BunMetadata) u.getProductById(bunid);
			return Response.ok().entity(bun).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("User with id=" + userid + " not found in baker registry");
			throw new WebApplicationException(builder.build());
		}
	}
	
	
	@GET
	@Path("/users/{userid}/apps/{appid}")
	@Produces("application/json")
	public Response getAppofUser(@PathParam("userid")int userid, @PathParam("appid")int appid) {
		logger.info("getAppofUser for userid: " + userid + ", appid=" + appid);
		BakerUser u = bakerRepositoryRef.getUserByID(userid);

		if (u != null) {
			ApplicationMetadata appmeta = (ApplicationMetadata) u.getProductById(appid);
			return Response.ok().entity(appmeta).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("User with id=" + userid + " not found in baker registry");
			throw new WebApplicationException(builder.build());
		}
	}
	

	@POST
	@Path("/users/{userid}/buns/")
	@Consumes("multipart/form-data")
	public Response addBunMetadata(@PathParam("userid") int userid,
			@Multipart(value = "bunname", type = "text/plain") String bunname,
			@Multipart(value = "shortDescription", type = "text/plain") String shortDescription,
			@Multipart(value = "longDescription", type = "text/plain") String longDescription,
			@Multipart(value = "version", type = "text/plain") String version,  
			@Multipart(value = "categories", type = "text/plain") String categories, 
			@Multipart(value = "uploadedBunIcon") Attachment image,
			@Multipart(value = "uploadedBunFile") Attachment bunFile) {


		
		BunMetadata sm = new BunMetadata();
		sm = (BunMetadata) addNewProductData(sm, userid, bunname, shortDescription, longDescription, version, categories, image, bunFile);

		
		return Response.ok().entity(sm).build();

	}

	
	private Product addNewProductData(Product sm, int userid, String prodName, String shortDescription, String longDescription, String version,
			String categories, Attachment image, Attachment bunFile) {
		String uuid = UUID.randomUUID().toString();
		String imageFileNamePosted = getFileName(image.getHeaders());
		
		logger.info("bunname = " + prodName);
		logger.info("version = " + version);
		logger.info("shortDescription = " + shortDescription);
		logger.info("longDescription = " + longDescription);
		logger.info("image = " + imageFileNamePosted);
		
		sm.setUuid(uuid);
		sm.setName(prodName);
		sm.setShortDescription(shortDescription);
		sm.setLongDescription(longDescription);
		sm.setVersion(version);
		sm.setDateCreated(new Date());
		sm.setDateUpdated(new Date());
		
		String[] catIDs = categories.split(",");
		for (String catid : catIDs) {
			Category category = bakerRepositoryRef.getCategoryByID( Integer.valueOf(catid) );		
			sm.addCategory(category);
		}

		URI endpointUrl = uri.getBaseUri();

		String tempDir = METADATADIR + uuid + File.separator;
		try {
			Files.createDirectories(Paths.get(tempDir));

			if (!imageFileNamePosted.equals("")) {
				String imgfile = saveFile(image, tempDir + imageFileNamePosted);
				logger.info("imgfile saved to = " + imgfile);
				sm.setIconsrc(endpointUrl + "repo/images/" + uuid + File.separator + imageFileNamePosted);
			}

			

			if (bunFile!=null){
				String bunFileNamePosted = getFileName(bunFile.getHeaders());
				logger.info("bunFile = " + bunFileNamePosted);
				if (!bunFileNamePosted.equals("")) {
					String bunfilepath = saveFile(bunFile, tempDir + bunFileNamePosted);
					logger.info("bunfilepath saved to = " + bunfilepath);
					sm.setPackageLocation(endpointUrl + "repo/packages/" + uuid + File.separator + bunFileNamePosted);
				}
			}
			
			

		} catch (IOException e) {
			e.printStackTrace();
		}

		// Save now bun for User
		BakerUser bunOwner = bakerRepositoryRef.getUserByID(userid);
		bunOwner.addProduct(sm);
		bakerRepositoryRef.updateUserInfo(userid, bunOwner);
		return sm;
	}

	@PUT
	@Path("/buns/{bid}")
	@Consumes("multipart/form-data")
	public Response updateBunMetadata(@PathParam("bid") int bid, 
			@Multipart(value = "userid", type = "text/plain") int userid,
			@Multipart(value = "bunname", type = "text/plain") String bunname, 
			@Multipart(value = "bunid", type = "text/plain") int bunid,
			@Multipart(value = "bunuuid", type = "text/plain") String uuid,
			@Multipart(value = "shortDescription", type = "text/plain") String shortDescription,
			@Multipart(value = "longDescription", type = "text/plain") String longDescription,
			@Multipart(value = "version", type = "text/plain") String version, 
			@Multipart(value = "categories", type = "text/plain") String categories,
			@Multipart(value = "uploadedBunIcon") Attachment image,
			@Multipart(value = "uploadedBunFile") Attachment bunFile) {

		

		BunMetadata sm = (BunMetadata) bakerRepositoryRef.getProductByID(bunid);
		sm = (BunMetadata) updateProductMetadata(sm, userid, bunname, uuid, shortDescription, longDescription, version, categories, image, bunFile);
		
		
		return Response.ok().entity(sm).build();

	}

	// Buns related API

	private Product updateProductMetadata(Product sm, int userid, String bunname, String uuid, String shortDescription, String longDescription,
			String version, String categories, Attachment image, Attachment bunFile) {
		String imageFileNamePosted = getFileName(image.getHeaders());
		
		logger.info("userid = " + userid);
		logger.info("bunname = " + bunname);
		logger.info("bunid = " + sm.getId());
		
		logger.info("bunuuid = " + uuid);
		logger.info("version = " + version);
		logger.info("shortDescription = " + shortDescription);
		logger.info("longDescription = " + longDescription);
		logger.info("image = " + imageFileNamePosted);

		// Save now bun for User
		BakerUser bunOwner = bakerRepositoryRef.getUserByID(userid);
		sm.setShortDescription(shortDescription);
		sm.setLongDescription(longDescription);
		sm.setVersion(version);
		sm.setName(bunname);
		sm.setOwner(bunOwner);
		sm.setDateUpdated(new Date());

		
		//first remove the bun from the previous category
		List<Category> cats = sm.getCategories();
		List<Category> catsToUpdate = new ArrayList<Category>();
		for (Category category : cats) {
			catsToUpdate.add(category);
		}		
		
		for (Category c : catsToUpdate) {
			c.removeProduct(sm);
			sm.removeCategory(c);
			bakerRepositoryRef.updateCategoryInfo( c );
		}
		
		String[] catIDs = categories.split(",");
		for (String catid : catIDs) {
			//and now add the new one
			Category category = bakerRepositoryRef.getCategoryByID(Integer.valueOf(catid));
			sm.addCategory(category);
		}


		URI endpointUrl = uri.getBaseUri();

		String tempDir = METADATADIR + uuid + File.separator;
		try {
			Files.createDirectories(Paths.get(tempDir));

			if (!imageFileNamePosted.equals("unknown")) {
				String imgfile = saveFile(image, tempDir + imageFileNamePosted);
				logger.info("imgfile saved to = " + imgfile);
				sm.setIconsrc(endpointUrl + "repo/images/" + uuid + File.separator + imageFileNamePosted);
			}

			if (bunFile!=null){
				String bunFileNamePosted = getFileName(bunFile.getHeaders());
				logger.info("bunFile = " + bunFileNamePosted);
				if (!bunFileNamePosted.equals("unknown")) {
					String bunfilepath = saveFile(bunFile, tempDir + bunFileNamePosted);
					logger.info("bunfilepath saved to = " + bunfilepath);
					sm.setPackageLocation(endpointUrl + "repo/packages/" + uuid + File.separator + bunFileNamePosted);
				}
			}

		} catch (IOException e) {
			
			e.printStackTrace();
		}

		bakerRepositoryRef.updateProductInfo(sm);

		if (bunOwner.getProductById(sm.getId()) == null)
			bunOwner.addProduct(sm);
		bakerRepositoryRef.updateUserInfo(userid, bunOwner);
		return sm;
	}

	@GET
	@Path("/buns")
	@Produces("application/json")
	public Response getBuns(@QueryParam("categoryid") Long categoryid) {
		logger.info("getBuns categoryid="+categoryid);

		List<BunMetadata> buns = bakerRepositoryRef.getBuns(categoryid);
		return Response.ok().entity(buns).build();

	}

	@GET
	@Path("/images/{uuid}/{imgfile}")
	@Produces("image/*")
	public Response getEntityImage(@PathParam("uuid") String uuid, @PathParam("imgfile") String imgfile) {
		logger.info("getEntityImage of uuid: " + uuid);
		String imgAbsfile = METADATADIR + uuid + File.separator + imgfile;
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

		String bunAbsfile = METADATADIR + uuid + File.separator + bunfile;
		logger.info("Bun RESOURCE FILE: " + bunAbsfile);
		File file = new File(bunAbsfile);

		if ((uuid.equals("77777777-668b-4c75-99a9-39b24ed3d8be")) || (uuid.equals("22cab8b8-668b-4c75-99a9-39b24ed3d8be"))) {
			URL res = getClass().getResource("/files/" + bunfile);
			logger.info("TEST LOCAL RESOURCE FILE: " + res);
			file = new File(res.getFile());
		}

		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition", "attachment; filename=" + file.getName());
		return response.build();
	}

	

	@GET
	@Path("/buns/{bunid}")
	@Produces("application/json")
	public Response getBunMetadataByID(@PathParam("bunid") int bunid) {
		logger.info("getBunMetadataByID  bunid=" + bunid);
		BunMetadata bun = (BunMetadata) bakerRepositoryRef.getProductByID(bunid);

		if (bun != null) {
			return Response.ok().entity(bun).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("bun with id=" + bunid + " not found in baker registry");
			throw new WebApplicationException(builder.build());
		}
	}

	@GET
	@Path("/buns/uuid/{uuid}")
	@Produces("application/json")
	public Response getBunMetadataByUUID(@PathParam("uuid") String uuid) {

		logger.info("Received GET for bun uuid: " + uuid);
		BunMetadata bun = null;

		URI endpointUrl = uri.getBaseUri();
		if (uuid.equals("77777777-668b-4c75-99a9-39b24ed3d8be")) {
			bun = new BunMetadata();
			bun.setUuid(uuid);
			bun.setName("IntegrTestLocal example service");
			bun.setShortDescription("An example local service");
			bun.setVersion("1.0.0");
			bun.setIconsrc("");
			bun.setLongDescription("");

			bun.setPackageLocation(endpointUrl + "repo/packages/77777777-668b-4c75-99a9-39b24ed3d8be/examplebun.tar.gz");
			// }else if (uuid.equals("12cab8b8-668b-4c75-99a9-39b24ed3d8be")) {
			// bun = new BunMetadata(uuid, "AN example service");
			// bun.setShortDescription("An example local service");
			// bun.setVersion("1.0.0rc1");
			// bun.setIconsrc("");
			// bun.setLongDescription("");
			// //URI endpointUrl = uri.getBaseUri();
			//
			// bun.setPackageLocation( endpointUrl +"repo/packages/12cab8b8-668b-4c75-99a9-39b24ed3d8be/examplebun.tar.gz");
		} else if (uuid.equals("22cab8b8-668b-4c75-99a9-39b24ed3d8be")) {
			bun = new BunMetadata();
			bun.setUuid(uuid);
			bun.setName("IntegrTestLocal example ErrInstall service");
			bun.setShortDescription("An example ErrInstall local service");
			bun.setVersion("1.0.0");
			bun.setIconsrc("");
			bun.setLongDescription("");
			// URI endpointUrl = uri.getBaseUri();

			bun.setPackageLocation(endpointUrl + "repo/packages/22cab8b8-668b-4c75-99a9-39b24ed3d8be/examplebunErrInstall.tar.gz");
		} else {
			bun = (BunMetadata) bakerRepositoryRef.getProductByUUID(uuid);
		}

		if (bun != null) {
			return Response.ok().entity(bun).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("Installed bun with uuid=" + uuid + " not found in local registry");
			throw new WebApplicationException(builder.build());
		}

	}

	private String saveFile(Attachment att, String filePath) {
		DataHandler handler = att.getDataHandler();
		try {
			InputStream stream = handler.getInputStream();
			MultivaluedMap map = att.getHeaders();
			File f = new File(filePath);
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

	//Sessions related API
	
//	@OPTIONS
//	@Path("/sessions/")
//	@Produces("application/json")
//	@Consumes("application/json")
//	@LocalPreflight
//	public Response addUserSessionOption(){
//		
//
//		logger.info("Received OPTIONS  addUserSessionOption ");
//		String origin = headers.getRequestHeader("Origin").get(0);
//        if (origin != null) {
//            return Response.ok()
//                           .header(CorsHeaderConstants.HEADER_AC_ALLOW_METHODS, "GET POST DELETE PUT HEAD OPTIONS")
//                           .header(CorsHeaderConstants.HEADER_AC_ALLOW_CREDENTIALS, "true")
//                           .header(CorsHeaderConstants.HEADER_AC_ALLOW_HEADERS, "Origin, X-Requested-With, Content-Type, Accept")
//                           .header(CorsHeaderConstants.HEADER_AC_ALLOW_ORIGIN, origin)
//                           .build();
//        } else {
//            return Response.ok().build();
//        }
//	}

	
	@POST
	@Path("/sessions/")
	@Produces("application/json")
	@Consumes("application/json")
	public Response addUserSession(UserSession userSession) {

		logger.info("Received POST addUserSession usergetUsername: " + userSession.getUsername());
		logger.info("DANGER, REMOVE Received POST addUserSession password: " + userSession.getPassword());
		
		if (securityContext!=null){
			if (securityContext.getUserPrincipal()!=null)
				logger.info(" securityContext.getUserPrincipal().toString() >" + securityContext.getUserPrincipal().toString()+"<");
		
		}


		Subject currentUser = SecurityUtils.getSubject();
		if (currentUser !=null){
			AuthenticationToken token =	new UsernamePasswordToken(  userSession.getUsername(), userSession.getPassword());
			try {
				currentUser.login(token);
				BakerUser bakerUser = bakerRepositoryRef.getUserByName( userSession.getUsername() );
				userSession.setBakerUser(bakerUser );				
				userSession.setPassword("");;//so not tosend in response
				
				logger.info(" currentUser = " + currentUser.toString() );
				logger.info( "User [" + currentUser.getPrincipal() + "] logged in successfully." );
				logger.info(" currentUser  employee  = " + currentUser.hasRole("employee")  );
				logger.info(" currentUser  boss  = " + currentUser.hasRole("boss")  );
				
				return Response.ok().entity(userSession).build();
				}
				catch (AuthenticationException ae) {
					
					return Response.status(Status.UNAUTHORIZED).build();
				} 			
		}
		
		
		return Response.status(Status.UNAUTHORIZED).build();
	}

	@GET
	@Path("/sessions/logout")
	@Produces("application/json")
	public Response logoutUser() {

		logger.info("Received logoutUser " );
		
		if (securityContext!=null){
			if (securityContext.getUserPrincipal()!=null)
				logger.info(" securityContext.getUserPrincipal().toString() >" + securityContext.getUserPrincipal().toString()+"<");
		
			SecurityUtils.getSubject().logout();
		}
		
		return Response.ok().build();
	}
		
	
	//THIS IS NOT USED
	@GET
	@Path("/sessions/")
	@Produces("application/json")
	public Response getUserSessions() {

		logger.info("Received GET addUserSession usergetUsername: " );
		logger.info("Received GET addUserSession password: " );
		
		if (securityContext!=null){
			if (securityContext.getUserPrincipal()!=null)
				logger.info(" securityContext.getUserPrincipal().toString() >" + securityContext.getUserPrincipal().toString()+"<");
		
		}


		Subject currentUser = SecurityUtils.getSubject();
		if ((currentUser !=null) && (currentUser.getPrincipal() !=null)){

//				logger.info(" currentUser = " + currentUser.toString() );
//				logger.info( "User [" + currentUser.getPrincipal() + "] logged in successfully." );
//				logger.info(" currentUser  employee  = " + currentUser.hasRole("employee")  );
//				logger.info(" currentUser  boss  = " + currentUser.hasRole("boss")  );
				
				return Response.ok().build();
		}
		
		
		return Response.status(Status.UNAUTHORIZED).build();
	}	
	
	//Subscribed MAchines related API
	
	@GET
	@Path("/subscribedmachines/")
	@Produces("application/json")
	public Response getSubscribedMachines() {
		return Response.ok().entity(bakerRepositoryRef.getSubscribedMachinesAsCollection()).build();
	}

	@GET
	@Path("/subscribedmachines/{smId}")
	@Produces("application/json")
	public Response getSubscribedMachineById(@PathParam("smId") int smId) {
		SubscribedMachine sm = bakerRepositoryRef.getSubscribedMachineByID(smId);

		if (sm != null) {
			return Response.ok().entity(sm).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("SubscribedMachine" + smId + " not found in baker registry");
			throw new WebApplicationException(builder.build());
		}
	}

	
	
	@POST
	@Path("/subscribedmachines/")
	@Produces("application/json")
	@Consumes("application/json")
	public Response addSubscribedMachine(SubscribedMachine sm) {
		
		SubscribedMachine u = bakerRepositoryRef.addSubscribedMachine(sm);

		if (u != null) {
			return Response.ok().entity(u).build();
		} else {
			ResponseBuilder builder = Response.status(Status.INTERNAL_SERVER_ERROR);
			builder.entity("Requested SubscribedMachine with rls=" + sm.getURL() + " cannot be saved");
			throw new WebApplicationException(builder.build());
		}
	}

	@PUT
	@Path("/subscribedmachines/{smId}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response updateSubscribedMachine(@PathParam("smId")int smId, SubscribedMachine sm) {
		logger.info("Received SubscribedMachine for user: " + sm.getURL());

		SubscribedMachine previouSM = bakerRepositoryRef.getSubscribedMachineByID(smId);

		

		SubscribedMachine u = bakerRepositoryRef.updateSubscribedMachineInfo(smId, sm);

		if (u != null) {
			return Response.ok().entity(u).build();
		} else {
			ResponseBuilder builder = Response.status(Status.INTERNAL_SERVER_ERROR);
			builder.entity("Requested SubscribedMachine with url=" + sm.getURL()+" cannot be updated");
			throw new WebApplicationException(builder.build());
		}
	}

	@DELETE
	@Path("/subscribedmachines/{smId}")
	@Produces("application/json")
	public Response deleteSubscribedMachine(@PathParam("smId")int smId) {
		logger.info("Received SubscribedMachine for userid: " + smId);

		bakerRepositoryRef.deleteSubscribedMachine(smId);

		return Response.ok().build();
	}
	
	
	//Applications related API

	@GET
	@Path("/apps")
	@Produces("application/json")
	public Response getApps(@QueryParam("categoryid") Long categoryid) {
		logger.info("getApps categoryid="+categoryid);
		List<ApplicationMetadata> buns = bakerRepositoryRef.getApps(categoryid);
		return Response.ok().entity(buns).build();
	}


	@GET
	@Path("/apps/{appid}")
	@Produces("application/json")
	public Response getAppMetadataByID(@PathParam("appid") int appid) {
		logger.info("getAppMetadataByID  appid=" + appid);
		ApplicationMetadata app = (ApplicationMetadata) bakerRepositoryRef.getProductByID(appid);

		if (app != null) {
			return Response.ok().entity(app).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("App with id=" + appid + " not found in baker registry");
			throw new WebApplicationException(builder.build());
		}
	}
	

	@GET
	@Path("/apps/uuid/{uuid}")
	@Produces("application/json")
	public Response getAppMetadataByUUID(@PathParam("uuid") String uuid) {
		logger.info("Received GET for app uuid: " + uuid);
		ApplicationMetadata app = null;

		URI endpointUrl = uri.getBaseUri();
		app = (ApplicationMetadata) bakerRepositoryRef.getProductByUUID(uuid);

		if (app != null) {
			return Response.ok().entity(app).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("Installed app with uuid=" + uuid + " not found in local registry");
			throw new WebApplicationException(builder.build());
		}

	}
	

	@POST
	@Path("/users/{userid}/apps/")
	@Consumes("multipart/form-data")
	public Response addAppMetadata(@PathParam("userid") int userid, 
			@Multipart(value = "appname", type = "text/plain") String appname, 
			@Multipart(value = "shortDescription", type = "text/plain") String shortDescription, 
			@Multipart(value = "longDescription", type = "text/plain") String longDescription, 
			@Multipart(value = "version", type = "text/plain") String version, 
			@Multipart(value = "categories", type = "text/plain") String categories, 
			@Multipart(value = "uploadedAppIcon") Attachment image) {
		
		

		ApplicationMetadata sm = new ApplicationMetadata();
		sm = (ApplicationMetadata) addNewProductData(sm, userid, appname, shortDescription, longDescription, version, categories, image, null);

		return Response.ok().entity(sm).build();
	}
	
	@PUT
	@Path("/apps/{aid}")
	@Consumes("multipart/form-data")
	public Response updateAppMetadata(@PathParam("aid") int aid, 
			@Multipart(value = "userid", type = "text/plain")int userid, 
			@Multipart(value = "appname", type = "text/plain")String appname, 
			@Multipart(value = "appid", type = "text/plain") int appid, 
			@Multipart(value = "appuuid", type = "text/plain") String uuid, 
			@Multipart(value = "shortDescription", type = "text/plain") String shortDescription, 
			@Multipart(value = "longDescription", type = "text/plain") String longDescription, 
			@Multipart(value = "version", type = "text/plain") String version,
			@Multipart(value = "categories", type = "text/plain") String categories,
			@Multipart(value = "uploadedAppIcon") Attachment image){
		
		

		ApplicationMetadata appmeta = (ApplicationMetadata) bakerRepositoryRef.getProductByID(appid);
		appmeta = (ApplicationMetadata) updateProductMetadata(appmeta, userid, appname, uuid, shortDescription, longDescription, 
				version, categories, image, null);
		
		return Response.ok().entity(appmeta).build();
	}


	
	

	@GET
	@Path("/categories/")
	@Produces("application/json")
	public Response getCategories() {
		return Response.ok().entity(bakerRepositoryRef.getCategories()).build();
	}


	@POST
	@Path("/categories/")
	@Produces("application/json")
	@Consumes("application/json")
	public Response addCategory(Category c) {
		Category u = bakerRepositoryRef.addCategory(c);

		if (u != null) {
			return Response.ok().entity(u).build();
		} else {
			ResponseBuilder builder = Response.status(Status.INTERNAL_SERVER_ERROR);
			builder.entity("Requested Category with name=" + c.getName() + " cannot be installed");
			throw new WebApplicationException(builder.build());
		}
	}

	@PUT
	@Path("/categories/{catid}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response updateCategory(@PathParam("catid")int catid, Category c) {
		Category previousCategory = bakerRepositoryRef.getCategoryByID(catid);		

		Category u = bakerRepositoryRef.updateCategoryInfo(c);

		if (u != null) {
			return Response.ok().entity(u).build();
		} else {
			ResponseBuilder builder = Response.status(Status.INTERNAL_SERVER_ERROR);
			builder.entity("Requested Category with name=" + c.getName()+" cannot be updated");
			throw new WebApplicationException(builder.build());
		}

		
	}

	@DELETE
	@Path("/categories/{catid}")
	public Response deleteCategory(@PathParam("catid") int catid) {
		Category category = bakerRepositoryRef.getCategoryByID(catid);
		if ((category.getProducts().size()>0) ){
			ResponseBuilder builder = Response.status(Status.METHOD_NOT_ALLOWED );
			builder.entity("The category has assigned elements. You cannot delete it!");
			throw new WebApplicationException(builder.build());
		}else{		
			bakerRepositoryRef.deleteCategory(catid);
			return Response.ok().build();
		}
	}


	@GET
	@Path("/categories/{catid}")
	@Produces("application/json")
	public Response getCategoryById(@PathParam("catid") int catid) {
		Category sm = bakerRepositoryRef.getCategoryByID(catid);

		if (sm != null) {
			return Response.ok().entity(sm).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("Category " + catid + " not found in baker registry");
			throw new WebApplicationException(builder.build());
		}
	}

	//Widgets related API

	@GET
	@Path("/widgets")
	@Produces("application/json")
	public Response getWidgets(@QueryParam("categoryid") Long categoryid) {
		logger.info("getWidgets categoryid="+categoryid);
		List<Widget> w = bakerRepositoryRef.getWidgets(categoryid);
		return Response.ok().entity(w).build();
	}

	/////////////WIDGETS related

	@GET
	@Path("/users/{userid}/widgets")
	@Produces("application/json")
	public Response getAllWidgetsofUser(@PathParam("userid") int userid) {
		logger.info("getAllWidgetsofUser for userid: " + userid);
		BakerUser u = bakerRepositoryRef.getUserByID(userid);

		if (u != null) {
			List<Product> prods = u.getProducts();
			List<Widget> widgets = new ArrayList<Widget>();
			for (Product p : prods) {
				if (p instanceof BunMetadata)
					widgets.add(  (Widget) p );
			}

			return Response.ok().entity(widgets).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("User with id=" + userid + " not found in baker registry");
			throw new WebApplicationException(builder.build());
		}
	}


	@GET
	@Path("/widgets/{widgetid}")
	@Produces("application/json")

	public Response getWidgetByID(@PathParam("widgetid") int widgetid) {
		logger.info("getWidgetByID  widgetid=" + widgetid);
		Widget w = (Widget) bakerRepositoryRef.getProductByID(widgetid);

		if (w != null) {
			return Response.ok().entity(w).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("widget with id=" + widgetid + " not found in baker registry");
			throw new WebApplicationException(builder.build());
		}
	}

	@GET
	@Path("/widgets/uuid/{uuid}")
	@Produces("application/json")
	public Response getWidgetUUID(@PathParam("uuid") String uuid) {
		logger.info("Received GET for Widget uuid: " + uuid);
		Widget w = null;

		URI endpointUrl = uri.getBaseUri();
		w = (Widget) bakerRepositoryRef.getProductByUUID(uuid);

		if (w != null) {
			return Response.ok().entity(w).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("Widget with uuid=" + uuid + " not found in local registry");
			throw new WebApplicationException(builder.build());
		}

	}
	
	@GET
	@Path("/users/{userid}/widgets/{widgetid}")
	@Produces("application/json")
	public Response getWidgetofUser(@PathParam("userid")int userid, @PathParam("widgetid")int widgetid) {
		logger.info("getWidgetofUser for userid: " + userid + ", widgetid=" + widgetid);
		BakerUser u = bakerRepositoryRef.getUserByID(userid);

		if (u != null) {
			Widget w = (Widget) u.getProductById(widgetid);
			return Response.ok().entity(w).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("User with id=" + userid + " not found in baker registry");
			throw new WebApplicationException(builder.build());
		}
	}

	@PUT
	@Path("/widgets/{wid}")
	@Consumes("multipart/form-data")
	public Response updateWidget(@PathParam("wid") int wid, 
			@Multipart(value = "userid", type = "text/plain")int userid, 
			@Multipart(value = "widgetname", type = "text/plain")String widgetname, 
			@Multipart(value = "url", type = "text/plain")String url, 
			@Multipart(value = "widgetid", type = "text/plain") int widgetid, 
			@Multipart(value = "widgetuuid", type = "text/plain") String uuid, 
			@Multipart(value = "shortDescription", type = "text/plain") String shortDescription, 
			@Multipart(value = "longDescription", type = "text/plain") String longDescription, 
			@Multipart(value = "version", type = "text/plain") String version,
			@Multipart(value = "categories", type = "text/plain") String categories,
			@Multipart(value = "uploadedWidgetIcon") Attachment image,
			@Multipart(value = "uploadedWidgetFile") Attachment bunFile){ 
		
		Widget appmeta = (Widget) bakerRepositoryRef.getProductByID(widgetid);
		appmeta.setURL(url);
		appmeta = (Widget) updateProductMetadata(appmeta, userid, widgetname, uuid, 
				shortDescription, longDescription, 
				version, categories, image, bunFile);
		
		return Response.ok().entity(appmeta).build();
	}

	@POST
	@Path("/users/{userid}/widgets/")
	@Consumes("multipart/form-data")
	public Response addWidget( @PathParam("userid") int userid,
			@Multipart(value = "widgetname", type = "text/plain")String widgetname, 
			@Multipart(value = "url", type = "text/plain")String url, 
			@Multipart(value = "shortDescription", type = "text/plain") String shortDescription, 
			@Multipart(value = "longDescription", type = "text/plain") String longDescription, 
			@Multipart(value = "version", type = "text/plain") String version,
			@Multipart(value = "categories", type = "text/plain") String categories,
			@Multipart(value = "uploadedWidgetIcon") Attachment image,
			@Multipart(value = "uploadedWidgetFile") Attachment bunFile){ 
		

		Widget sm = new Widget();
		sm.setURL(url);
		sm = (Widget) addNewProductData(sm, userid, widgetname, shortDescription, longDescription, 
				version, categories, image, bunFile);

		return Response.ok().entity(sm).build();
	}
	

	@DELETE
	@Path("/widgets/{widgetid}")
	public void deleteWidget(@PathParam("widgetid") int widgetid) {
		bakerRepositoryRef.deleteProduct(widgetid);
		
	}

	@DELETE
	@Path("/apps/{appid}")
	public void deleteApp(@PathParam("appid") int appid) {
		bakerRepositoryRef.deleteProduct(appid);
		
	}
	
	@DELETE
	@Path("/buns/{bunid}")
	public void deleteBun(@PathParam("bunid") int bunid) {
		bakerRepositoryRef.deleteProduct(bunid);
	}

	@DELETE
	@Path("/courses/{courseid}")
	public void deleteCourse(@PathParam("courseid") int courseid) {
		bakerRepositoryRef.deleteProduct(courseid);
	}

	@GET
	@Path("/courses")
	@Produces("application/json")
	public Response getCourses(@QueryParam("categoryid") Long categoryid) {
		logger.info("getCourses categoryid="+categoryid);
		List<Course> courses = bakerRepositoryRef.getCourses(categoryid);
		return Response.ok().entity(courses).build();
	}


	@GET
	@Path("/courses/{courseid}")
	@Produces("application/json")
	public Response getCoursetByID(@PathParam("courseid") int courseid) {
		logger.info("getCoursetByID  courseid=" + courseid);
		Course c = (Course) bakerRepositoryRef.getProductByID(courseid);

		if (c != null) {
			return Response.ok().entity(c).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("Course with id=" + courseid + " not found in baker registry");
			throw new WebApplicationException(builder.build());
		}
	}


	@GET
	@Path("/courses/uuid/{uuid}")
	@Produces("application/json")
	public Response getCourseUUID(@PathParam("uuid") String uuid) {

		Course c = (Course) bakerRepositoryRef.getProductByUUID(uuid);
		if (c != null) {
			return Response.ok().entity(c).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("Course with uuid=" + uuid + " not found in local registry");
			throw new WebApplicationException(builder.build());
		}
	}




	@PUT
	@Path("/courses/{cid}")
	@Consumes("multipart/form-data")
	public Response updateCourse(
			@PathParam("cid") int cid, 
			@Multipart(value = "userid", type = "text/plain") int userid,
			@Multipart(value = "coursename", type = "text/plain") String coursename, 
			@Multipart(value = "courseid", type = "text/plain") int bunid,
			@Multipart(value = "courseuuid", type = "text/plain") String uuid,
			@Multipart(value = "shortDescription", type = "text/plain") String shortDescription,
			@Multipart(value = "longDescription", type = "text/plain") String longDescription,
			@Multipart(value = "version", type = "text/plain") String version, 
			@Multipart(value = "categories", type = "text/plain") String categories,
			@Multipart(value = "uploadedCourseIcon") Attachment image,
			@Multipart(value = "uploadedCourseFile") Attachment courseFile
			) {

		Course c = (Course) bakerRepositoryRef.getProductByID(bunid);
		c = (Course) updateProductMetadata(c, userid, coursename, uuid, 
				shortDescription, longDescription, version, categories, image, courseFile);
		
		
		return Response.ok().entity(c).build();
	}

	

	@POST
	@Path("/users/{userid}/courses/")
	@Consumes("multipart/form-data")
	public Response addCourse(
			@PathParam("userid") int userid,
			@Multipart(value = "coursename", type = "text/plain") String coursename,
			@Multipart(value = "shortDescription", type = "text/plain") String shortDescription,
			@Multipart(value = "longDescription", type = "text/plain") String longDescription,
			@Multipart(value = "version", type = "text/plain") String version,  
			@Multipart(value = "categories", type = "text/plain") String categories, 
			@Multipart(value = "uploadedCourseIcon") Attachment image,
			@Multipart(value = "uploadedCourseFile") Attachment courseFile
	) {
		Course c = new Course();
		c = (Course) addNewProductData(c, userid, 
				coursename, shortDescription, longDescription, version, 
				categories, image, courseFile);
		
		return Response.ok().entity(c).build();
	}



	@GET
	@Path("/users/{userid}/courses/{courseid}")
	@Produces("application/json")
	public Response getCourseofUser(
			@PathParam("userid") int userid, 
			@PathParam("courseid") int courseid) {
		logger.info("getCourseofUser for userid: " + userid + ", courseid=" + courseid);
		BakerUser u = bakerRepositoryRef.getUserByID(userid);

		if (u != null) {
			Course course = (Course) u.getProductById(courseid);
			return Response.ok().entity(course).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("User with id=" + userid + " not found in baker registry");
			throw new WebApplicationException(builder.build());
		}
	}
	
	@GET
	@Path("/users/{userid}/courses")
	@Produces("application/json")
	public Response getAllCoursesofUser(@PathParam("userid") int userid) {
		logger.info("getAllCoursesofUser for userid: " + userid);
		BakerUser u = bakerRepositoryRef.getUserByID(userid);

		if (u != null) {
			List<Product> prods = u.getProducts();
			List<Course> courses = new ArrayList<Course>();
			for (Product p : prods) {
				if (p instanceof BunMetadata)
					courses.add(  (Course) p );
			}

			return Response.ok().entity(courses).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("User with id=" + userid + " not found in baker registry");
			throw new WebApplicationException(builder.build());
		}
	}
	

	


}
