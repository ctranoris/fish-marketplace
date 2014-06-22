package gr.upatras.ece.nam.baker.impl;

import gr.upatras.ece.nam.baker.model.IRepositoryWebClient;
import gr.upatras.ece.nam.baker.model.ServiceMetadata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.MappingJsonFactory;

public class RepositoryWebClient implements IRepositoryWebClient {

	private static final transient Log logger = LogFactory
			.getLog(RepositoryWebClient.class.getName());

	@Override
	public ServiceMetadata fetchMetadata(UUID uuid, String url) {
		logger.info("fetchMetadata from: " + url + " , for uuid=" + uuid);

		try {
			List<Object> providers = new ArrayList<Object>();
			providers.add(new org.codehaus.jackson.jaxrs.JacksonJsonProvider());
			WebClient client = WebClient.create(url, providers);
			Response r = client.accept("application/json").type("application/json").get();
			MappingJsonFactory factory = new MappingJsonFactory();
			JsonParser parser  = factory.createJsonParser((InputStream) r.getEntity());
			ServiceMetadata output = parser.readValueAs(ServiceMetadata.class);
			return output;
			
			
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Path fetchPackageFromLocation(UUID uuid, String packageLocation) {

		logger.info("fetchPackageFromLocation: " + packageLocation );

		Path tempDir;
		try {
			WebClient client = WebClient.create(packageLocation);
			Response r = client.get();
			
			InputStream inputStream = (InputStream) r.getEntity();
			

			tempDir = Files.createTempDirectory("baker");
			File destFile = new File(tempDir+"/"+uuid+"/bun.tar.gz" );
			Files.createDirectory( Paths.get( tempDir+"/"+uuid ) );
			Path targetPath = destFile.toPath();
            OutputStream output = new FileOutputStream(targetPath.toFile());			
			IOUtils.copy(inputStream, output);			
			
			return targetPath;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		return null;
	}
	
	

}
