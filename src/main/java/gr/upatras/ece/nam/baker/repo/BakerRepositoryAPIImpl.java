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

import gr.upatras.ece.nam.baker.fiware.FIWARECloudAccess;
import gr.upatras.ece.nam.baker.fiware.FIWAREUser;
import gr.upatras.ece.nam.baker.fiware.FIWAREUtils;
import gr.upatras.ece.nam.baker.fiware.OAuthClientManager;
import gr.upatras.ece.nam.baker.model.ApplicationMetadata;
import gr.upatras.ece.nam.baker.model.BakerProperty;
import gr.upatras.ece.nam.baker.model.BakerUser;
import gr.upatras.ece.nam.baker.model.BunMetadata;
import gr.upatras.ece.nam.baker.model.Category;
import gr.upatras.ece.nam.baker.model.DeployArtifact;
import gr.upatras.ece.nam.baker.model.DeployContainer;
import gr.upatras.ece.nam.baker.model.DeploymentDescriptor;
import gr.upatras.ece.nam.baker.model.DeploymentDescriptorStatus;
import gr.upatras.ece.nam.baker.model.IBakerRepositoryAPI;
import gr.upatras.ece.nam.baker.model.InstalledBun;
import gr.upatras.ece.nam.baker.model.InstalledBunStatus;
import gr.upatras.ece.nam.baker.model.Product;
import gr.upatras.ece.nam.baker.model.ProductExtensionItem;
import gr.upatras.ece.nam.baker.model.SubscribedResource;
import gr.upatras.ece.nam.baker.model.UserSession;
import gr.upatras.ece.nam.baker.util.EmailUtil;

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
import java.util.ServiceLoader;
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
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.cxf.jaxrs.utils.multipart.AttachmentUtils;
import org.apache.cxf.rs.security.cors.CorsHeaderConstants;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing;
import org.apache.cxf.rs.security.cors.LocalPreflight;
import org.apache.cxf.rs.security.oauth2.common.ClientAccessToken;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeGrant;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.ObjectMapper;

import com.woorea.openstack.keystone.model.Access;
import com.woorea.openstack.keystone.model.Access.Service.Endpoint;
import com.woorea.openstack.keystone.model.Tenant;
import com.woorea.openstack.keystone.model.Access.Service;
import com.woorea.openstack.nova.model.Server;
import com.woorea.openstack.nova.model.Servers;

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
	private OAuthClientManager oAuthClientManagerRef;

	public static final String KEYSTONE_AUTH_URL = "http://cloud.lab.fi-ware.org:4730/v2.0";

	// BakerUser related API

	/*************** Users API *************************/

	@GET
	@Path("/admin/users/")
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

	@GET
	@Path("/admin/users/{userid}")
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
	@Path("/admin/users/")
	@Produces("application/json")
	@Consumes("application/json")
	public Response addUser(BakerUser user) {

		logger.info("Received POST for usergetUsername: " + user.getUsername());
		// logger.info("Received POST for usergetPassword: " + user.getPassword());
		// logger.info("Received POST for usergetOrganization: " + user.getOrganization());

		if ((user.getUsername() == null) || (user.getUsername().equals("") || (user.getEmail() == null) || (user.getEmail().equals("")))) {
			ResponseBuilder builder = Response.status(Status.BAD_REQUEST);
			builder.entity("New user with username=" + user.getUsername() + " cannot be registered");
			logger.info("New user with username=" + user.getUsername() + " cannot be registered BAD_REQUEST.");
			throw new WebApplicationException(builder.build());
		}

		BakerUser u = bakerRepositoryRef.getUserByUsername(user.getUsername());
		if (u != null) {
			return Response.status(Status.BAD_REQUEST).entity("Username exists").build();
		}

		u = bakerRepositoryRef.getUserByEmail(user.getEmail());
		if (u != null) {
			return Response.status(Status.BAD_REQUEST).entity("Email exists").build();
		}

		u = bakerRepositoryRef.addBakerUserToUsers(user);

		if (u != null) {
			return Response.ok().entity(u).build();
		} else {
			ResponseBuilder builder = Response.status(Status.INTERNAL_SERVER_ERROR);
			builder.entity("Requested user with username=" + user.getUsername() + " cannot be installed");
			return builder.build();
		}
	}

	@POST
	@Path("/register/")
	@Produces("application/json")
	@Consumes("multipart/form-data")
	public Response addNewRegisterUser(List<Attachment> ats) {

		BakerUser user = new BakerUser();
		user.setName(getAttachmentStringValue("name", ats));
		user.setUsername(getAttachmentStringValue("username", ats));
		user.setPassword(getAttachmentStringValue("userpassword", ats));
		user.setOrganization(getAttachmentStringValue("userorganization", ats) + "^^" + getAttachmentStringValue("randomregid", ats));
		user.setEmail(getAttachmentStringValue("useremail", ats));
		user.setActive(false);// in any case the user should be not active
		user.setRole("ROLE_DEVELOPER"); // otherwise in post he can choose ROLE_BOSS, and the immediately register :-)

		String msg = getAttachmentStringValue("emailmessage", ats);
		logger.info("Received register for usergetUsername: " + user.getUsername());

		Response r = addUser(user);

		if (r.getStatusInfo().getStatusCode() == Status.OK.getStatusCode()) {
			logger.info("Email message: " + msg);
			EmailUtil.SendRegistrationActivationEmail(user.getEmail(), msg);
		}

		return r;
	}

	@PUT
	@Path("/admin/users/{userid}")
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
	@Path("/admin/users/{userid}")
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
					buns.add((BunMetadata) p);
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
					apps.add((ApplicationMetadata) p);
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
	public Response getAppofUser(@PathParam("userid") int userid, @PathParam("appid") int appid) {
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

	// BUNS API

	private Product addNewProductData(Product prod, Attachment image, Attachment bunFile, List<Attachment> screenshots) {

		String uuid = UUID.randomUUID().toString();

		logger.info("prodname = " + prod.getName());
		logger.info("version = " + prod.getVersion());
		logger.info("shortDescription = " + prod.getShortDescription());
		logger.info("longDescription = " + prod.getLongDescription());

		prod.setUuid(uuid);
		prod.setDateCreated(new Date());
		prod.setDateUpdated(new Date());

		// String[] catIDs = categories.split(",");
		// for (String catid : catIDs) {
		// Category category = bakerRepositoryRef.getCategoryByID( Integer.valueOf(catid) );
		// prod.addCategory(category);
		// }

		// for (ProductExtensionItem e : extensions) {
		//
		// }
		//
		// String[] exts = extensions.split(",");
		// for (String extparmval : exts) {
		// String[] i = extparmval.split("=");
		// prod.addExtensionItem(i[0], i[1]);
		// }

		URI endpointUrl = uri.getBaseUri();

		String tempDir = METADATADIR + uuid + File.separator;
		try {
			Files.createDirectories(Paths.get(tempDir));

			if (image != null) {
				String imageFileNamePosted = getFileName(image.getHeaders());
				logger.info("image = " + imageFileNamePosted);
				if (!imageFileNamePosted.equals("")) {
					String imgfile = saveFile(image, tempDir + imageFileNamePosted);
					logger.info("imgfile saved to = " + imgfile);
					prod.setIconsrc(endpointUrl + "repo/images/" + uuid + File.separator + imageFileNamePosted);
				}
			}

			if (bunFile != null) {
				String bunFileNamePosted = getFileName(bunFile.getHeaders());
				logger.info("bunFile = " + bunFileNamePosted);
				if (!bunFileNamePosted.equals("")) {
					String bunfilepath = saveFile(bunFile, tempDir + bunFileNamePosted);
					logger.info("bunfilepath saved to = " + bunfilepath);
					prod.setPackageLocation(endpointUrl + "repo/packages/" + uuid + File.separator + bunFileNamePosted);
				}
			}

			List<Attachment> ss = screenshots;
			String screenshotsFilenames = "";
			int i = 1;
			for (Attachment shot : ss) {
				String shotFileNamePosted = getFileName(shot.getHeaders());
				logger.info("Found screenshot image shotFileNamePosted = " + shotFileNamePosted);
				logger.info("shotFileNamePosted = " + shotFileNamePosted);
				if (!shotFileNamePosted.equals("")) {
					shotFileNamePosted = "shot" + i + "_" + shotFileNamePosted;
					String shotfilepath = saveFile(shot, tempDir + shotFileNamePosted);
					logger.info("shotfilepath saved to = " + shotfilepath);
					shotfilepath = endpointUrl + "repo/images/" + uuid + File.separator + shotFileNamePosted;
					screenshotsFilenames += shotfilepath + ",";
					i++;
				}
			}
			if (screenshotsFilenames.length() > 0)
				screenshotsFilenames = screenshotsFilenames.substring(0, screenshotsFilenames.length() - 1);

			prod.setScreenshots(screenshotsFilenames);

		} catch (IOException e) {
			e.printStackTrace();
		}

		// we must replace given product categories with the ones from our DB
		for (Category c : prod.getCategories()) {
			Category catToUpdate = bakerRepositoryRef.getCategoryByID(c.getId());
			// logger.info("BEFORE PROD SAVE, category "+catToUpdate.getName()+"  contains Products: "+ catToUpdate.getProducts().size() );
			prod.getCategories().set(prod.getCategories().indexOf(c), catToUpdate);

		}

		// Save now bun for User
		BakerUser bunOwner = bakerRepositoryRef.getUserByID(prod.getOwner().getId());
		bunOwner.addProduct(prod);
		prod.setOwner(bunOwner); // replace given owner with the one from our DB

		BakerUser owner = bakerRepositoryRef.updateUserInfo(prod.getOwner().getId(), bunOwner);
		Product registeredProd = bakerRepositoryRef.getProductByUUID(uuid);

		// now fix category references
		for (Category c : registeredProd.getCategories()) {
			Category catToUpdate = bakerRepositoryRef.getCategoryByID(c.getId());
			catToUpdate.addProduct(registeredProd);
			bakerRepositoryRef.updateCategoryInfo(catToUpdate);
		}

		return registeredProd;
	}

	/******************* Buns API ***********************/

	@GET
	@Path("/buns")
	@Produces("application/json")
	public Response getAllBuns(@QueryParam("categoryid") Long categoryid) {
		logger.info("getBuns categoryid=" + categoryid);

		List<BunMetadata> buns = bakerRepositoryRef.getBuns(categoryid);
		return Response.ok().entity(buns).build();

	}

	@GET
	@Path("/admin/buns")
	@Produces("application/json")
	public Response getBuns(@QueryParam("categoryid") Long categoryid) {
		logger.info("getBuns categoryid=" + categoryid);

		BakerUser u = bakerRepositoryRef.getUserBySessionID(ws.getHttpServletRequest().getSession().getId());

		if (u != null) {
			List<BunMetadata> buns;

			if (u.getRole().equals("ROLE_BOSS")) {
				buns = bakerRepositoryRef.getBuns(categoryid);
			} else {
				buns = bakerRepositoryRef.getBunsByUserID((long) u.getId());
			}

			return Response.ok().entity(buns).build();

		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("User not found in baker registry or not logged in");
			throw new WebApplicationException(builder.build());
		}

	}

	@POST
	@Path("/admin/buns/")
	@Consumes("multipart/form-data")
	public Response addBunMetadata(List<Attachment> ats) {

		BakerUser u = bakerRepositoryRef.getUserBySessionID(ws.getHttpServletRequest().getSession().getId());

		if (u == null) {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("User not found in baker registry or not logged in ");
			throw new WebApplicationException(builder.build());
		}

		BunMetadata bun = new BunMetadata();

		try {
			MappingJsonFactory factory = new MappingJsonFactory();
			JsonParser parser = factory.createJsonParser(getAttachmentStringValue("bun", ats));
			bun = parser.readValueAs(BunMetadata.class);

			logger.info("Received @POST for bun : " + bun.getName());
			logger.info("Received @POST for bun.extensions : " + bun.getExtensions());

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		bun = (BunMetadata) addNewProductData(bun,

		getAttachmentByName("prodIcon", ats), getAttachmentByName("prodFile", ats), getListOfAttachmentsByName("screenshots", ats));

		return Response.ok().entity(bun).build();

	}

	@PUT
	@Path("/admin/buns/{bid}")
	@Consumes("multipart/form-data")
	public Response updateBunMetadata(@PathParam("bid") int bid, List<Attachment> ats) {

		BunMetadata bun = new BunMetadata();

		try {
			MappingJsonFactory factory = new MappingJsonFactory();
			JsonParser parser = factory.createJsonParser(getAttachmentStringValue("bun", ats));
			bun = parser.readValueAs(BunMetadata.class);

			logger.info("Received @POST for bun : " + bun.getName());
			logger.info("Received @POST for bun.extensions : " + bun.getExtensions());

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// BunMetadata sm = (BunMetadata) bakerRepositoryRef.getProductByID(bid);
		bun = (BunMetadata) updateProductMetadata(bun, getAttachmentByName("prodIcon", ats), getAttachmentByName("prodFile", ats),
				getListOfAttachmentsByName("screenshots", ats));

		return Response.ok().entity(bun).build();

	}

	// Buns related API

	private Product updateProductMetadata(Product prod, Attachment image, Attachment prodFile, List<Attachment> screenshots) {

		logger.info("userid = " + prod.getOwner().getId());
		logger.info("bunname = " + prod.getName());
		logger.info("bunid = " + prod.getId());

		logger.info("bunuuid = " + prod.getUuid());
		logger.info("version = " + prod.getVersion());
		logger.info("shortDescription = " + prod.getShortDescription());
		logger.info("longDescription = " + prod.getLongDescription());

		// get User
		BakerUser bunOwner = bakerRepositoryRef.getUserByID(prod.getOwner().getId());
		prod.setOwner(bunOwner); // replace given owner with the one from our DB

		prod.setDateUpdated(new Date());

		// first remove all references of the product from the previous categories
		Product prodPreUpdate = (Product) bakerRepositoryRef.getProductByID(prod.getId());
		for (Category c : prodPreUpdate.getCategories()) {
			// logger.info("Will remove product "+prodPreUpdate.getName()+ ", from Previous Category "+c.getName() );
			c.removeProduct(prodPreUpdate);
			bakerRepositoryRef.updateCategoryInfo(c);
		}

		// we must replace API given product categories with the ones from our DB
		for (Category c : prod.getCategories()) {
			Category catToUpdate = bakerRepositoryRef.getCategoryByID(c.getId());
			// logger.info("BEFORE PROD SAVE, category "+catToUpdate.getName()+"  contains Products: "+ catToUpdate.getProducts().size() );
			prod.getCategories().set(prod.getCategories().indexOf(c), catToUpdate);
		}

		URI endpointUrl = uri.getBaseUri();

		String tempDir = METADATADIR + prod.getUuid() + File.separator;
		try {
			Files.createDirectories(Paths.get(tempDir));

			if (image != null) {
				String imageFileNamePosted = getFileName(image.getHeaders());
				logger.info("image = " + imageFileNamePosted);
				if (!imageFileNamePosted.equals("unknown")) {
					String imgfile = saveFile(image, tempDir + imageFileNamePosted);
					logger.info("imgfile saved to = " + imgfile);
					prod.setIconsrc(endpointUrl + "repo/images/" + prod.getUuid() + File.separator + imageFileNamePosted);
				}
			}

			if (prodFile != null) {
				String bunFileNamePosted = getFileName(prodFile.getHeaders());
				logger.info("bunFile = " + bunFileNamePosted);
				if (!bunFileNamePosted.equals("unknown")) {
					String bunfilepath = saveFile(prodFile, tempDir + bunFileNamePosted);
					logger.info("bunfilepath saved to = " + bunfilepath);
					prod.setPackageLocation(endpointUrl + "repo/packages/" + prod.getUuid() + File.separator + bunFileNamePosted);
				}
			}

			List<Attachment> ss = screenshots;
			String screenshotsFilenames = "";
			int i = 1;
			for (Attachment shot : ss) {
				String shotFileNamePosted = getFileName(shot.getHeaders());
				logger.info("Found screenshot image shotFileNamePosted = " + shotFileNamePosted);
				logger.info("shotFileNamePosted = " + shotFileNamePosted);
				if (!shotFileNamePosted.equals("")) {
					shotFileNamePosted = "shot" + i + "_" + shotFileNamePosted;
					String shotfilepath = saveFile(shot, tempDir + shotFileNamePosted);
					logger.info("shotfilepath saved to = " + shotfilepath);
					shotfilepath = endpointUrl + "repo/images/" + prod.getUuid() + File.separator + shotFileNamePosted;
					screenshotsFilenames += shotfilepath + ",";
					i++;
				}
			}
			if (screenshotsFilenames.length() > 0)
				screenshotsFilenames = screenshotsFilenames.substring(0, screenshotsFilenames.length() - 1);

			prod.setScreenshots(screenshotsFilenames);

		} catch (IOException e) {

			e.printStackTrace();
		}

		// save product
		prod = bakerRepositoryRef.updateProductInfo(prod);

		// now fix category product references
		for (Category catToUpdate : prod.getCategories()) {
			Product p = bakerRepositoryRef.getProductByID(prod.getId());
			catToUpdate.addProduct(p);
			bakerRepositoryRef.updateCategoryInfo(catToUpdate);
		}

		if (bunOwner.getProductById(prod.getId()) == null)
			bunOwner.addProduct(prod);
		bakerRepositoryRef.updateUserInfo(prod.getOwner().getId(), bunOwner);
		return prod;
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

	@DELETE
	@Path("/admin/buns/{bunid}")
	public void deleteBun(@PathParam("bunid") int bunid) {
		bakerRepositoryRef.deleteProduct(bunid);
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
	@Path("/admin/buns/{bunid}")
	@Produces("application/json")
	public Response getAdminBunMetadataByID(@PathParam("bunid") int bunid) {

		return getBunMetadataByID(bunid);
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

	public BakerRepository getBakerRepositoryRef() {
		return bakerRepositoryRef;
	}

	public void setBakerRepositoryRef(BakerRepository bakerRepositoryRef) {
		this.bakerRepositoryRef = bakerRepositoryRef;
	}

	public void setoAuthClientManagerRef(OAuthClientManager oAuthClientManagerRef) {
		this.oAuthClientManagerRef = oAuthClientManagerRef;
	}

	// Sessions related API

	// @OPTIONS
	// @Path("/sessions/")
	// @Produces("application/json")
	// @Consumes("application/json")
	// @LocalPreflight
	// public Response addUserSessionOption(){
	//
	//
	// logger.info("Received OPTIONS  addUserSessionOption ");
	// String origin = headers.getRequestHeader("Origin").get(0);
	// if (origin != null) {
	// return Response.ok()
	// .header(CorsHeaderConstants.HEADER_AC_ALLOW_METHODS, "GET POST DELETE PUT HEAD OPTIONS")
	// .header(CorsHeaderConstants.HEADER_AC_ALLOW_CREDENTIALS, "true")
	// .header(CorsHeaderConstants.HEADER_AC_ALLOW_HEADERS, "Origin, X-Requested-With, Content-Type, Accept")
	// .header(CorsHeaderConstants.HEADER_AC_ALLOW_ORIGIN, origin)
	// .build();
	// } else {
	// return Response.ok().build();
	// }
	// }

	@POST
	@Path("/sessions/")
	@Produces("application/json")
	@Consumes("application/json")
	public Response addUserSession(UserSession userSession) {

		logger.info("Received POST addUserSession usergetUsername: " + userSession.getUsername());
		// logger.info("DANGER, REMOVE Received POST addUserSession password: " + userSession.getPassword());

		if (securityContext != null) {
			if (securityContext.getUserPrincipal() != null)
				logger.info(" securityContext.getUserPrincipal().toString() >" + securityContext.getUserPrincipal().toString() + "<");

		}

		Subject currentUser = SecurityUtils.getSubject();
		if (currentUser != null) {
			AuthenticationToken token = new UsernamePasswordToken(userSession.getUsername(), userSession.getPassword());
			try {
				currentUser.login(token);
				BakerUser bakerUser = bakerRepositoryRef.getUserByUsername(userSession.getUsername());
				bakerUser.setCurrentSessionID(ws.getHttpServletRequest().getSession().getId());
				userSession.setBakerUser(bakerUser);
				userSession.setPassword("");
				;// so not tosend in response

				logger.info(" currentUser = " + currentUser.toString());
				logger.info("User [" + currentUser.getPrincipal() + "] logged in successfully.");

				bakerRepositoryRef.updateUserInfo(bakerUser.getId(), bakerUser);

				return Response.ok().entity(userSession).build();
			} catch (AuthenticationException ae) {

				return Response.status(Status.UNAUTHORIZED).build();
			}
		}

		return Response.status(Status.UNAUTHORIZED).build();
	}

	@GET
	@Path("/sessions/logout")
	@Produces("application/json")
	public Response logoutUser() {

		logger.info("Received logoutUser ");

		if (securityContext != null) {
			if (securityContext.getUserPrincipal() != null)
				logger.info(" securityContext.getUserPrincipal().toString() >" + securityContext.getUserPrincipal().toString() + "<");

			SecurityUtils.getSubject().logout();
		}

		return Response.ok().build();
	}

	// THIS IS NOT USED
	@GET
	@Path("/sessions/")
	@Produces("application/json")
	public Response getUserSessions() {

		logger.info("Received GET addUserSession usergetUsername: ");
		logger.info("Received GET addUserSession password: ");

		if (securityContext != null) {
			if (securityContext.getUserPrincipal() != null)
				logger.info(" securityContext.getUserPrincipal().toString() >" + securityContext.getUserPrincipal().toString() + "<");

		}

		Subject currentUser = SecurityUtils.getSubject();
		if ((currentUser != null) && (currentUser.getPrincipal() != null)) {

			// logger.info(" currentUser = " + currentUser.toString() );
			// logger.info( "User [" + currentUser.getPrincipal() + "] logged in successfully." );
			// logger.info(" currentUser  employee  = " + currentUser.hasRole("employee") );
			// logger.info(" currentUser  boss  = " + currentUser.hasRole("boss") );

			return Response.ok().build();
		}

		return Response.status(Status.UNAUTHORIZED).build();
	}

	// Subscribed resources related API

	@GET
	@Path("/admin/subscribedresources/")
	@Produces("application/json")
	public Response getSubscribedResources() {

		BakerUser u = bakerRepositoryRef.getUserBySessionID(ws.getHttpServletRequest().getSession().getId());

		if (u != null) {

			if (u.getRole().equals("ROLE_BOSS")) {
				return Response.ok().entity(bakerRepositoryRef.getSubscribedResourcesAsCollection()).build(); // return all
			} else
				return Response.ok().entity(u.getSubscribedResources()).build();

		}

		ResponseBuilder builder = Response.status(Status.NOT_FOUND);
		builder.entity("User not found in baker registry or not logged in");
		throw new WebApplicationException(builder.build());

	}

	@GET
	@Path("/admin/subscribedresources/{smId}")
	@Produces("application/json")
	public Response getSubscribedResourceById(@PathParam("smId") int smId) {

		BakerUser u = bakerRepositoryRef.getUserBySessionID(ws.getHttpServletRequest().getSession().getId());

		SubscribedResource sm = bakerRepositoryRef.getSubscribedResourceByID(smId);

		if ((sm != null) && (u != null)) {

			if ((u.getRole().equals("ROLE_BOSS")) || (sm.getOwner().getId() == u.getId()))
				return Response.ok().entity(sm).build();

		}

		ResponseBuilder builder = Response.status(Status.NOT_FOUND);
		builder.entity("SubscribedResource" + smId + " not found in baker registry");
		throw new WebApplicationException(builder.build());

	}

	@POST
	@Path("/admin/subscribedresources/")
	@Produces("application/json")
	@Consumes("application/json")
	public Response addSubscribedResource(SubscribedResource sm) {

		BakerUser u = sm.getOwner();
		u = bakerRepositoryRef.getUserByID(sm.getOwner().getId());

		if (u != null) {
			sm.setOwner(u);

			u.getSubscribedResources().add(sm);
			u = bakerRepositoryRef.updateUserInfo(u.getId(), u);

			return Response.ok().entity(sm).build();
		} else {
			ResponseBuilder builder = Response.status(Status.INTERNAL_SERVER_ERROR);
			builder.entity("Requested SubscribedResource with rls=" + sm.getURL() + " cannot be registered under not found user");
			throw new WebApplicationException(builder.build());
		}
	}

	@PUT
	@Path("/admin/subscribedresources/{smId}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response updateSubscribedResource(@PathParam("smId") int smId, SubscribedResource sm) {
		logger.info("Received SubscribedResource for user: " + sm.getURL());

		BakerUser u = bakerRepositoryRef.getUserBySessionID(ws.getHttpServletRequest().getSession().getId());

		BakerUser reattachedUser = bakerRepositoryRef.getUserByID(sm.getOwner().getId());
		sm.setOwner(reattachedUser);

		if (u != null) {

			if ((u.getRole().equals("ROLE_BOSS")) || (sm.getOwner().getId() == u.getId())) {

				SubscribedResource sr = bakerRepositoryRef.updateSubscribedResourceInfo(smId, sm);
				return Response.ok().entity(u).build();
			}

		}

		ResponseBuilder builder = Response.status(Status.INTERNAL_SERVER_ERROR);
		builder.entity("Requested SubscribedResource with url=" + sm.getURL() + " cannot be updated");
		throw new WebApplicationException(builder.build());

	}

	@DELETE
	@Path("/admin/subscribedresources/{smId}")
	@Produces("application/json")
	public Response deleteSubscribedResource(@PathParam("smId") int smId) {
		logger.info("Received SubscribedResource for userid: " + smId);

		BakerUser u = bakerRepositoryRef.getUserBySessionID(ws.getHttpServletRequest().getSession().getId());

		SubscribedResource sm = bakerRepositoryRef.getSubscribedResourceByID(smId);
		if (u != null) {

			if ((u.getRole().equals("ROLE_BOSS")) || (sm.getOwner().getId() == u.getId())) {
				bakerRepositoryRef.deleteSubscribedResource(smId);
				return Response.ok().build();

			}
		}

		ResponseBuilder builder = Response.status(Status.INTERNAL_SERVER_ERROR);
		builder.entity("Requested SubscribedResource with id=" + smId + " cannot be deleted");
		throw new WebApplicationException(builder.build());
	}

	// Applications related API

	@GET
	@Path("/admin/apps")
	@Produces("application/json")
	public Response getApps(@QueryParam("categoryid") Long categoryid) {

		BakerUser u = bakerRepositoryRef.getUserBySessionID(ws.getHttpServletRequest().getSession().getId());

		if (u != null) {
			List<ApplicationMetadata> apps;

			if (u.getRole().equals("ROLE_BOSS")) {
				apps = bakerRepositoryRef.getApps(categoryid);
			} else {
				apps = bakerRepositoryRef.getAppsByUserID((long) u.getId());
			}

			return Response.ok().entity(apps).build();

		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("User not found in baker registry or not logged in");
			throw new WebApplicationException(builder.build());
		}

	}

	@GET
	@Path("/apps")
	@Produces("application/json")
	public Response getAllApps(@QueryParam("categoryid") Long categoryid) {
		logger.info("getApps categoryid=" + categoryid);
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
	@Path("/admin/apps/{appid}")
	@Produces("application/json")
	public Response getAdminAppMetadataByID(@PathParam("appid") int appid) {
		return getAppMetadataByID(appid);
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
	@Path("/admin/apps/")
	@Consumes("multipart/form-data")
	public Response addAppMetadata(List<Attachment> ats) {

		BakerUser u = bakerRepositoryRef.getUserBySessionID(ws.getHttpServletRequest().getSession().getId());

		if (u == null) {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("User not found in baker registry or not logged in ");
			throw new WebApplicationException(builder.build());
		}

		ApplicationMetadata app = new ApplicationMetadata();

		try {
			MappingJsonFactory factory = new MappingJsonFactory();
			JsonParser parser = factory.createJsonParser(getAttachmentStringValue("application", ats));
			app = parser.readValueAs(ApplicationMetadata.class);

			logger.info("Received @POST for app : " + app.getName());
			logger.info("Received @POST for app.containers : " + app.getContainers().size());
			logger.info("Received @POST for app.containers(0).name : " + app.getContainers().get(0).getName());

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// ApplicationMetadata sm = new ApplicationMetadata();
		app = (ApplicationMetadata) addNewProductData(app, getAttachmentByName("prodIcon", ats), getAttachmentByName("prodFile", ats),
				getListOfAttachmentsByName("screenshots", ats));

		return Response.ok().entity(app).build();

	}

	@PUT
	@Path("/admin/apps/{aid}")
	@Consumes("multipart/form-data")
	public Response updateAppMetadata(@PathParam("aid") int aid, List<Attachment> ats) {

		ApplicationMetadata appmeta = new ApplicationMetadata();

		try {
			MappingJsonFactory factory = new MappingJsonFactory();
			JsonParser parser = factory.createJsonParser(getAttachmentStringValue("application", ats));
			appmeta = parser.readValueAs(ApplicationMetadata.class);

			logger.info("Received @POST for app : " + appmeta.getName());
			logger.info("Received @POST for app.containers : " + appmeta.getContainers().size());

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// ApplicationMetadata appmeta = (ApplicationMetadata) bakerRepositoryRef.getProductByID(aid);

		appmeta = (ApplicationMetadata) updateProductMetadata(appmeta, getAttachmentByName("prodIcon", ats), getAttachmentByName("prodFile", ats),
				getListOfAttachmentsByName("screenshots", ats));

		return Response.ok().entity(appmeta).build();
	}

	@DELETE
	@Path("/admin/apps/{appid}")
	public void deleteApp(@PathParam("appid") int appid) {
		bakerRepositoryRef.deleteProduct(appid);

	}

	// categories API
	@GET
	@Path("/categories/")
	@Produces("application/json")
	public Response getCategories() {
		return Response.ok().entity(bakerRepositoryRef.getCategories()).build();
	}

	@GET
	@Path("/admin/categories/")
	@Produces("application/json")
	public Response getAdminCategories() {
		return Response.ok().entity(bakerRepositoryRef.getCategories()).build();
	}

	@POST
	@Path("/admin/categories/")
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
	@Path("/admin/categories/{catid}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response updateCategory(@PathParam("catid") int catid, Category c) {
		Category previousCategory = bakerRepositoryRef.getCategoryByID(catid);

		Category u = bakerRepositoryRef.updateCategoryInfo(c);

		if (u != null) {
			return Response.ok().entity(u).build();
		} else {
			ResponseBuilder builder = Response.status(Status.INTERNAL_SERVER_ERROR);
			builder.entity("Requested Category with name=" + c.getName() + " cannot be updated");
			throw new WebApplicationException(builder.build());
		}

	}

	@DELETE
	@Path("/admin/categories/{catid}")
	public Response deleteCategory(@PathParam("catid") int catid) {
		Category category = bakerRepositoryRef.getCategoryByID(catid);
		if ((category.getProducts().size() > 0)) {
			ResponseBuilder builder = Response.status(Status.METHOD_NOT_ALLOWED);
			builder.entity("The category has assigned elements. You cannot delete it!");
			throw new WebApplicationException(builder.build());
		} else {
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

	@GET
	@Path("/admin/categories/{catid}")
	@Produces("application/json")
	public Response getAdminCategoryById(@PathParam("catid") int catid) {
		return getCategoryById(catid);
	}

	// Attachment utils ///////////////////////
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

	public String getAttachmentStringValue(String name, List<Attachment> attachments) {

		Attachment att = getAttachmentByName(name, attachments);
		if (att != null) {
			return att.getObject(String.class);
		}
		return null;
	}

	public Attachment getAttachmentByName(String name, List<Attachment> attachments) {

		for (Attachment attachment : attachments) {
			String s = getAttachmentName(attachment.getHeaders());
			if ((s != null) && (s.equals(name)))
				return attachment;
		}

		return null;
	}

	private List<Attachment> getListOfAttachmentsByName(String name, List<Attachment> attachments) {

		List<Attachment> la = new ArrayList<Attachment>();
		for (Attachment attachment : attachments) {
			if (getAttachmentName(attachment.getHeaders()).equals(name))
				la.add(attachment);
		}
		return la;
	}

	private String getAttachmentName(MultivaluedMap<String, String> header) {

		if (header.getFirst("Content-Disposition") != null) {
			String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
			for (String filename : contentDisposition) {
				if ((filename.trim().startsWith("name"))) {
					String[] name = filename.split("=");
					String exactFileName = name[1].trim().replaceAll("\"", "");
					return exactFileName;
				}
			}
		}
		return null;
	}

	/***************************************** OAUTH2 FIWARE Related API *********************************************/

	@GET
	@Path("/oauth2/")
	@Produces("application/json")
	public Response oauth2Sessions(@QueryParam("oath2serverurl") String oath2serverurl, @QueryParam("oath2requestkey") String oath2requestkey) {

		// the params
		logger.info("Received GET oath2serverurl: " + oath2serverurl);
		logger.info("Received GET oath2requestkey: " + oath2requestkey);

		return Response.seeOther(oAuthClientManagerRef.getAuthorizationServiceURI(getCallbackURI(), oath2requestkey)).build();
	}

	@GET
	@Path("/oauth2/login")
	@Produces("text/html")
	// @Produces("application/json")
	public Response oauth2login(@QueryParam("code") String code) {

		// This one is the callback URL, which is called by the FIWARE OAUTH2 service
		logger.info("Received authorized request token code: " + code + ". Preparing AuthorizationCodeGrant header.");

		AuthorizationCodeGrant codeGrant = new AuthorizationCodeGrant(code, getCallbackURI());
		logger.info("Requesting OAuth server accessTokenService to replace an authorized request token with an access token");
		ClientAccessToken accessToken = oAuthClientManagerRef.getAccessToken(codeGrant);
		if (accessToken == null) {
			String msg = "NO_OAUTH_ACCESS_TOKEN, Problem replacing your authorization key for OAuth access token,  please report to baker admin";
			logger.info(msg);
			return Response.status(Status.UNAUTHORIZED).entity(msg).build();
		}

		try {
			logger.info("OAUTH2 accessTokenService accessToken = " + accessToken.toString());
			String authHeader = oAuthClientManagerRef.createAuthorizationHeader(accessToken);
			logger.info("OAUTH2 accessTokenService authHeader = " + authHeader);
			logger.info("accessToken getTokenType= " + accessToken.getTokenType());
			logger.info("accessToken getTokenKey= " + accessToken.getTokenKey());
			logger.info("accessToken getRefreshToken= " + accessToken.getRefreshToken());
			logger.info("accessToken getExpiresIn= " + accessToken.getExpiresIn());

			Tenant t = FIWARECloudAccess.getFirstTenant(accessToken.getTokenKey());
			FIWAREUser fu = FIWAREUtils.getFIWAREUser(authHeader, accessToken);
			fu.setxOAuth2Token(accessToken.getTokenKey());
			fu.setTenantName(t.getName());
			fu.setTenantId(t.getId());
			fu.setCloudToken(FIWARECloudAccess.getAccessModel(t, accessToken.getTokenKey()).getToken().getId());

			// check if user exists in Baker database
			BakerUser u = bakerRepositoryRef.getUserByUsername(fu.getNickName());

			String roamPassword = UUID.randomUUID().toString(); // creating a temporary session password, to login
			if (u == null) {
				u = new BakerUser(); // create as new user
				u.setEmail(fu.getEmail());
				u.setUsername(fu.getNickName());
				;
				u.setName(fu.getDisplayName());
				u.setOrganization("FI-WARE");
				u.setRole("SERVICE_PLATFORM_PROVIDER");
				u.setPassword(roamPassword);
				u.setCurrentSessionID(ws.getHttpServletRequest().getSession().getId());
				bakerRepositoryRef.addBakerUserToUsers(u);
			} else {
				u.setEmail(fu.getEmail());
				u.setName(fu.getDisplayName());
				u.setPassword(roamPassword);
				u.setOrganization("FI-WARE");
				u.setCurrentSessionID(ws.getHttpServletRequest().getSession().getId());
				u = bakerRepositoryRef.updateUserInfo(u.getId(), u);
			}

			UserSession userSession = new UserSession();
			userSession.setBakerUser(u);
			userSession.setPassword(roamPassword);
			userSession.setUsername(u.getUsername());
			userSession.setFIWAREUser(fu);

			Subject currentUser = SecurityUtils.getSubject();
			if (currentUser != null) {
				AuthenticationToken token = new UsernamePasswordToken(userSession.getUsername(), userSession.getPassword());
				try {
					currentUser.login(token);

				} catch (AuthenticationException ae) {

					return Response.status(Status.UNAUTHORIZED).build();
				}
			}

			userSession.setPassword("");// trick so not to send in response
			ObjectMapper mapper = new ObjectMapper();

			// see https://developer.mozilla.org/en-US/docs/Web/API/Window.postMessage
			// there are CORS issues so to do this trich the popup window communicates with the parent window ia this script
			String comScript = "<script type='text/javascript'>function receiveMessage(event){" + "event.source.postMessage('"
					+ mapper.writeValueAsString(userSession) + "', event.origin);" + "}" + "window.addEventListener('message', receiveMessage, false);"
					+ "</script>";

			return Response.ok("<html><body><p>Succesful Login</p>" + comScript + "</body></html>"

			).build();

		} catch (RuntimeException ex) {
			ex.printStackTrace();
			return Response.status(Status.UNAUTHORIZED).entity("USER Access problem").build();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return Response.ok().build();

	}

	private URI getCallbackURI() {
		return URI.create(uri.getBaseUri() + "repo/oauth2/login");
	}

	@GET
	@Path("/fiware/computeendpoints")
	@Produces("application/json")
	public Response getFIWAREServiceCatalogComputeEndpoints(@QueryParam("xauthtoken") String xauthtoken) {

		List<Endpoint> scatalog = FIWARECloudAccess.getServiceCatalogEndpointsOnlyCompute(xauthtoken);

		return Response.ok(scatalog).build();
	}

	@GET
	@Path("/fiware/servers")
	@Produces("application/json")
	public Response getFIWAREServiceComputeServers(@QueryParam("endPointPublicURL") String endPointPublicURL,
			@QueryParam("cloudAccessToken") String cloudAccessToken) {

		ArrayList<Server> servers = FIWARECloudAccess.getServers(endPointPublicURL, cloudAccessToken);

		return Response.ok(servers).build();
	}

	@GET
	@Path("/admin/properties/")
	@Produces("application/json")
	public Response getProperties() {
		return Response.ok().entity(bakerRepositoryRef.getProperties()).build();
	}

	@PUT
	@Path("/admin/properties/{propid}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response updateProperty(@PathParam("catid") int propid, BakerProperty p) {
		BakerProperty previousProperty = bakerRepositoryRef.getPropertyByID(propid);

		BakerProperty u = bakerRepositoryRef.updateProperty(p);

		if (u != null) {
			return Response.ok().entity(u).build();
		} else {
			ResponseBuilder builder = Response.status(Status.INTERNAL_SERVER_ERROR);
			builder.entity("Requested BakerProperty with name=" + p.getName() + " cannot be updated");
			throw new WebApplicationException(builder.build());
		}

	}

	@GET
	@Path("/admin/properties/{propid}")
	@Produces("application/json")
	public Response getPropertyById(@PathParam("propid") int propid) {
		BakerProperty sm = bakerRepositoryRef.getPropertyByID(propid);

		if (sm != null) {
			return Response.ok().entity(sm).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("BakerProperty " + propid + " not found in baker registry");
			throw new WebApplicationException(builder.build());
		}
	}

	@GET
	@Path("/admin/deployments")
	@Produces("application/json")
	public Response getAllDeploymentsofUser() {

		BakerUser u = bakerRepositoryRef.getUserBySessionID(ws.getHttpServletRequest().getSession().getId());

		if (u != null) {
			logger.info("getAllDeploymentsofUser for userid: " + u.getId());
			List<DeploymentDescriptor> deployments;

			if (u.getRole().equals("ROLE_BOSS")) {
				deployments = bakerRepositoryRef.getAllDeploymentDescriptors();
			} else {
				deployments = u.getDeployments();
			}

			return Response.ok().entity(deployments).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("User not found in baker registry or not logged in");
			throw new WebApplicationException(builder.build());
		}

	}

	@POST
	@Path("/admin/deployments")
	@Produces("application/json")
	@Consumes("application/json")
	public Response addDeployment(DeploymentDescriptor deployment) {

		BakerUser u = bakerRepositoryRef.getUserBySessionID(ws.getHttpServletRequest().getSession().getId());

		if (u != null) {
			logger.info("addDeployment for userid: " + u.getId());

			for (DeploymentDescriptor d : u.getDeployments()) {
				logger.info("deployment already for userid: " + d.getId());
			}

			deployment.setDateCreated(new Date());
			deployment.setStatus(DeploymentDescriptorStatus.PENDING_ADMIN_AUTH);

			u = bakerRepositoryRef.getUserByID(u.getId());
			deployment.setOwner(u); // reattach from the DB model
			u.getDeployments().add(deployment);

			ApplicationMetadata baseApplication = (ApplicationMetadata) bakerRepositoryRef.getProductByID(deployment.getBaseApplication().getId());
			deployment.setBaseApplication(baseApplication); // reattach from the DB model

			for (DeployContainer dc : deployment.getDeployContainers()) {
				dc.getTargetResource().setOwner(u);// reattach from the DB model, in case missing from the request
			}

			u = bakerRepositoryRef.updateUserInfo(u.getId(), u);

			return Response.ok().entity(deployment).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("User not found in baker registry or not logged in. DeploymentDescriptor not added.");
			throw new WebApplicationException(builder.build());
		}
	}

	@DELETE
	@Path("/admin/deployments/{id}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response deleteDeployment(@PathParam("id") int id) {
		BakerUser u = bakerRepositoryRef.getUserBySessionID(ws.getHttpServletRequest().getSession().getId());

		DeploymentDescriptor dep = bakerRepositoryRef.getDeploymentByID(id);
		if (u != null) {
			if  (  u.getRole().equals("ROLE_BOSS") ||  u.getId() == dep.getOwner().getId())    {
				bakerRepositoryRef.deleteDeployment(id);
				return Response.ok().build();
			}
		}

		ResponseBuilder builder = Response.status(Status.NOT_FOUND);
		builder.entity("User not found in baker registry or not logged in");
		throw new WebApplicationException(builder.build());
	}

	@GET
	@Path("/admin/deployments/{id}")
	@Produces("application/json")
	public Response getDeploymentById(@PathParam("id") int deploymentId) {

		BakerUser u = bakerRepositoryRef.getUserBySessionID(ws.getHttpServletRequest().getSession().getId());

		if (u != null) {
			logger.info("getDeploymentById for id: " + deploymentId);
			DeploymentDescriptor deployment = bakerRepositoryRef.getDeploymentByID(deploymentId);

			if ((u.getRole().equals("ROLE_BOSS")) || (deployment.getOwner().getId() == u.getId())) {
				return Response.ok().entity(deployment).build();
			}

		}

		ResponseBuilder builder = Response.status(Status.NOT_FOUND);
		builder.entity("User not found in baker registry or not logged in");
		throw new WebApplicationException(builder.build());

	}

	@PUT
	@Path("/admin/deployments/{id}")
	@Produces("application/json")
	@Consumes("application/json")
	public Response updateDeployment(@PathParam("id") int id, DeploymentDescriptor d, @QueryParam("action") String action) {

		BakerUser u = bakerRepositoryRef.getUserBySessionID(ws.getHttpServletRequest().getSession().getId());

		if ((u != null) ) { 

			if (action.equals("AUTH") && (u.getRole().equals("ROLE_BOSS")) ) // only admin can alter a deployment
				d.setStatus(DeploymentDescriptorStatus.QUEUED);
			else if (action.equals("UNINSTALL")  &&  (u.getRole().equals("ROLE_BOSS") ||  u.getId() == d.getOwner().getId())  )
				d.setStatus(DeploymentDescriptorStatus.UNINSTALLING);
			else if (action.equals("DENY") && (u.getRole().equals("ROLE_BOSS")) )
				d.setStatus(DeploymentDescriptorStatus.DENIED);

			BakerUser deploymentOwner = bakerRepositoryRef.getUserByID(d.getOwner().getId() );
			d.setOwner(deploymentOwner); // reattach from the DB model

			ApplicationMetadata baseApplication = (ApplicationMetadata) bakerRepositoryRef.getProductByID(d.getBaseApplication().getId());
			d.setBaseApplication(baseApplication); // reattach from the DB model

			for (DeployContainer dc : d.getDeployContainers()) {
				
				dc.getTargetResource().setOwner(deploymentOwner);// reattach from the DB model, in case missing from the request
			}

			DeploymentDescriptor deployment = bakerRepositoryRef.updateDeploymentDescriptor(d);

			logger.info("updateDeployment for id: " + d.getId());

			return Response.ok().entity(deployment).build();

		}

		ResponseBuilder builder = Response.status(Status.FORBIDDEN);
		builder.entity("User not found in baker registry or not logged in as admin");
		throw new WebApplicationException(builder.build());

	}

	@POST
	@Path("/registerresource/")
	@Produces("application/json")
	@Consumes("application/json")
	public Response addANewAnauthSubscribedResource(SubscribedResource sm) {

		logger.info("Received SubscribedResource for client: " + sm.getUuid() + ", URLs:" + sm.getURL() + ", OwnerID:" + sm.getOwner().getId());

		BakerUser u = sm.getOwner();
		u = bakerRepositoryRef.getUserByID(sm.getOwner().getId());

		if ((u != null) && (sm.getUuid() != null)) {

			SubscribedResource checkSM = bakerRepositoryRef.getSubscribedResourceByUUID(sm.getUuid());

			if (checkSM == null) {
				sm.setOwner(u);
				sm.setActive(false);
				u.getSubscribedResources().add(sm);
				u = bakerRepositoryRef.updateUserInfo(u.getId(), u);
				return Response.ok().entity(sm).build();
			} else {
				checkSM.setURL(sm.getURL());// update URL if changed
				// u = bakerRepositoryRef.updateUserInfo( u.getId(), u);
				checkSM = bakerRepositoryRef.updateSubscribedResourceInfo(checkSM.getId(), checkSM);
				return Response.ok().entity(checkSM).build();
			}

		} else {
			ResponseBuilder builder = Response.status(Status.INTERNAL_SERVER_ERROR);
			builder.entity("Requested SubscribedResource with rls=" + sm.getURL() + " cannot be registered under not found user");
			throw new WebApplicationException(builder.build());
		}
	}

	@GET
	@Path("/registerresource/deployments/target/uuid/{uuid}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getDeployContainerByTargetResourceUUID(@PathParam("uuid") String uuid) {

		SubscribedResource res = updateLastSeenResource(uuid);
		if (res!=null)
			logger.info("Received req for Deployent by client: " + res.getUuid() + ", URLs:" + res.getURL() + ", OwnerID:" + res.getOwner().getId());

		List<DeploymentDescriptor> deployments = bakerRepositoryRef.getAllDeploymentDescriptors();
		for (DeploymentDescriptor deploymentDescriptor : deployments) 
			if ((deploymentDescriptor.getStatus()!= DeploymentDescriptorStatus.PENDING_ADMIN_AUTH )&&
					(deploymentDescriptor.getStatus()!= DeploymentDescriptorStatus.DENIED )&&
					(deploymentDescriptor.getStatus()!= DeploymentDescriptorStatus.UNINSTALLED )){
				List<DeployContainer> dcs = deploymentDescriptor.getDeployContainers();
				for (DeployContainer dc : dcs) {
					if ((dc.getTargetResource()!=null) && (dc.getTargetResource().getUuid().equals(uuid))) {						
						dc.setMasterDeploymentStatus( deploymentDescriptor.getStatus() );
						return Response.ok().entity(dc).build();
					}
				}
			}

		ResponseBuilder builder = Response.status(Status.NOT_FOUND);
		builder.entity("Deploy Container for TargetResource not found");
		throw new WebApplicationException(builder.build());

	}

	// /registerresource/deployments/target/uuid/"+ clientUUID+"/installedbunuuid/"+installedBunUUID+"/status/"+status"

	@PUT
	@Path("/registerresource/deployments/target/uuid/{clientUUID}/installedbunuuid/{installedBunUUID}/status/{status}/deployContainerid/{cid}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response updateDeployContainerTargetResourceStatus(@PathParam("clientUUID") String clientUUID,
			@PathParam("installedBunUUID") String installedBunUUID, @PathParam("status") String status,@PathParam("cid") Long deployContainerId) {

		SubscribedResource res = updateLastSeenResource(clientUUID);
		if (res!=null)
			logger.info("Received ResourceStatus: " + status + ", for Deployent by client: " + res.getUuid() + ", URLs:" + res.getURL() 
					+ ", OwnerID:"+ res.getOwner().getId()
					+ ", installedBunUUID:"+ installedBunUUID);

		List<DeploymentDescriptor> deployments = bakerRepositoryRef.getAllDeploymentDescriptors();
		for (DeploymentDescriptor deploymentDescriptor : deployments) {
			List<DeployContainer> dcs = deploymentDescriptor.getDeployContainers();
			for (DeployContainer dc : dcs) {
				if ((deployContainerId == dc.getId()) && (dc.getTargetResource()!=null) && dc.getTargetResource().getUuid().equals(clientUUID)) {
					List<DeployArtifact> artifacts = dc.getDeployArtifacts();
					for (DeployArtifact deployArtifact : artifacts)
						if (deployArtifact.getUuid().equals(installedBunUUID)) {
							deployArtifact.setStatus(InstalledBunStatus.valueOf(status));
							
						}

					deploymentDescriptor.setStatus( resolveStatus(deploymentDescriptor) ); // we must write here code to properly find the status!
					bakerRepositoryRef.updateDeploymentDescriptor(deploymentDescriptor);
					
					return Response.status(Status.OK).build();
				}else{
					logger.info(" dc.getTargetResource()==null !! PROBLEM");
				}
			}

		}

		logger.info("Deploy Container for TargetResource not found");
		ResponseBuilder builder = Response.status(Status.NOT_FOUND);
		builder.entity("Deploy Container for TargetResource not found");
		throw new WebApplicationException(builder.build());

	}

	private DeploymentDescriptorStatus resolveStatus(DeploymentDescriptor deploymentDescriptor) {

		Boolean allInstalled = true;
		Boolean allUnInstalled = true;
		DeploymentDescriptorStatus status= deploymentDescriptor.getStatus();

		List<DeployContainer> containers = deploymentDescriptor.getDeployContainers();
		for (DeployContainer deployContainer : containers) {
			List<DeployArtifact> artifacts = deployContainer.getDeployArtifacts();
			for (DeployArtifact deployArtifact : artifacts) {
				if (deployArtifact.getStatus()!=InstalledBunStatus.STARTED)
					allInstalled= false;
				if (deployArtifact.getStatus()!=InstalledBunStatus.UNINSTALLED)
					allUnInstalled= false;

				if ((deployArtifact.getStatus()==InstalledBunStatus.FAILED))
					return DeploymentDescriptorStatus.FAILED;
				if ((deployArtifact.getStatus()==InstalledBunStatus.UNINSTALLING))
					return DeploymentDescriptorStatus.UNINSTALLING;
				if ((deployArtifact.getStatus()==InstalledBunStatus.CONFIGURING)|| 
						(deployArtifact.getStatus()==InstalledBunStatus.DOWNLOADING)|| 
						(deployArtifact.getStatus()==InstalledBunStatus.DOWNLOADED)|| 
						(deployArtifact.getStatus()==InstalledBunStatus.INSTALLING)|| 
						(deployArtifact.getStatus()==InstalledBunStatus.INSTALLED)|| 
						(deployArtifact.getStatus()==InstalledBunStatus.STARTING)
						)
					return DeploymentDescriptorStatus.INSTALLING;
			}
		}
		
		if (allInstalled) 
			return DeploymentDescriptorStatus.INSTALLED;
		else if (allUnInstalled) 
			return DeploymentDescriptorStatus.UNINSTALLED;
		else
			return status;
	}

	private SubscribedResource updateLastSeenResource(String clientUUID) {

		SubscribedResource res = bakerRepositoryRef.getSubscribedResourceByUUID(clientUUID);
		if (res != null) {
			res.setLastUpdate(new Date()); // each time Baker Client Polls marketplace, we update this Last seen of client
			BakerUser reattachedUser = bakerRepositoryRef.getUserByID(res.getOwner().getId());
			res.setOwner(reattachedUser);
			bakerRepositoryRef.updateSubscribedResourceInfo(res.getId(), res);
		}

		return res;
	}

}
