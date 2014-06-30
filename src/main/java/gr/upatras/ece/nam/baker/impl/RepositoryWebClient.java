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
			if ( r.getStatus() == Response.Status.OK.getStatusCode() ){
				MappingJsonFactory factory = new MappingJsonFactory();
				JsonParser parser  = factory.createJsonParser((InputStream) r.getEntity());
				ServiceMetadata output = parser.readValueAs(ServiceMetadata.class);
				return output;
			}
			
			
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
