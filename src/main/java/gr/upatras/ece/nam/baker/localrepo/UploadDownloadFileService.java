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

package gr.upatras.ece.nam.baker.localrepo;

import gr.upatras.ece.nam.baker.model.ServiceMetadata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;


@Path("/localrepo")
public class UploadDownloadFileService {

	@Context
	UriInfo uri;

	private static final transient Log logger = LogFactory.getLog(UploadDownloadFileService.class.getName());

	@GET
	@Path("/packages/{uuid}/{bunfile}")
	@Produces("application/gzip")
	public Response downloadFile(@PathParam("uuid") String uuid, @PathParam("bunfile") String bunfile) {

		logger.info("bunfile: " + bunfile);
		logger.info("uuid: " + uuid);
		URL res = getClass().getResource("/examplebun.tar.gz");
		logger.info("TEST RESOURCE FILE: " + res);

		File file = new File(res.getFile());
		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition", "attachment; filename=" + file.getName());
		return response.build();
	}

	@GET
	@Path("/iservices/{uuid}")
	@Produces("application/json")
	public Response getMetadata(@PathParam("uuid") String uuid) {

		logger.info("Received GET for uuid: " + uuid);
		ServiceMetadata sm = null;
		
		if (uuid.equals("12cab8b8-668b-4c75-99a9-39b24ed3d8be")) {
			sm = new ServiceMetadata(UUID.fromString(uuid), "Local example service");
			sm.setShortDescription("An example local service");
			sm.setVersion("1.0.0");
			sm.setIconsrc("");
			sm.setProvider("");
			sm.setLongDescription("");
			URI endpointUrl = uri.getBaseUri();
			
			sm.setPackageLocation(endpointUrl + "localrepo/packages/12cab8b8-668b-4c75-99a9-39b24ed3d8be/examplebun.tar.gz");
		}

		if (sm != null) {
			return Response.ok().entity(sm).build();
		} else {
			ResponseBuilder builder = Response.status(Status.NOT_FOUND);
			builder.entity("Installed service with uuid=" + uuid + " not found in local registry");
			throw new WebApplicationException(builder.build());
		}

	}

	// @POST
	// @Path("/uploadFile")
	// @Consumes(MediaType.MULTIPART_FORM_DATA)
	// public Response uploadFile(List<Attachment> attachments, @Context
	// HttpServletRequest request) {
	// for (Attachment attachment : attachments) {
	// DataHandler handler = attachment.getDataHandler();
	// try {
	// InputStream stream = handler.getInputStream();
	// MultivaluedMap<String, String> map = attachment.getHeaders();
	// System.out.println("fileName Here" + getFileName(map));
	// OutputStream out = new FileOutputStream(new File("C:/uploads/"
	// + getFileName(map)));
	//
	// int read = 0;
	// byte[] bytes = new byte[1024];
	// while ((read = stream.read(bytes)) != -1) {
	// out.write(bytes, 0, read);
	// }
	// stream.close();
	// out.flush();
	// out.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// return Response.ok("file uploaded").build();
	// }
	//
	// private String getFileName(MultivaluedMap<String, String> header) {
	// String[] contentDisposition = header.getFirst("Content-Disposition")
	// .split(";");
	// for (String filename : contentDisposition) {
	// if ((filename.trim().startsWith("filename"))) {
	// String[] name = filename.split("=");
	// String exactFileName = name[1].trim().replaceAll("\"", "");
	// return exactFileName;
	// }
	// }
	// return "unknown";
	// }
}
