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

package gr.upatras.ece.nam.baker;

import static org.junit.Assert.*;
import gr.upatras.ece.nam.baker.impl.BakerJpaController;
import gr.upatras.ece.nam.baker.model.InstalledBun;
import gr.upatras.ece.nam.baker.model.InstalledBunStatus;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BakerServiceRSIT {

	private static String endpointUrl;
	private static final transient Log logger = LogFactory.getLog(BakerServiceRSIT.class.getName());

	@Autowired
	private static BakerJpaController bakerJpaControllerTest;

	@BeforeClass
	public static void beforeClass() {
		endpointUrl = System.getProperty("service.url");
		// bakerJpaControllerTest.delete(message);
	}

	 @Test
	 public void testBakerRSInstallServiceNotFoundAndFail() throws Exception {
	
	 logger.info("Executing TEST = testBakerRSInstallServiceNotFound");
	 List<Object> providers = new ArrayList<Object>();
	 providers.add(new org.codehaus.jackson.jaxrs.JacksonJsonProvider());
	 String uuid = UUID.fromString("55cab8b8-668b-4c75-99a9-39b24ed3d8be").toString();
	 InstalledBun is = prepareInstalledService(uuid);
	
	 WebClient client = WebClient.create(endpointUrl + "/services/baker/api/ibuns/", providers);
	 Response r = client.accept("application/json")
	 .type("application/json")
	 .post(is);
	 assertEquals(Response.Status.OK.getStatusCode(), r.getStatus());
	
	
	 MappingJsonFactory factory = new MappingJsonFactory();
	 JsonParser parser = factory.createJsonParser((InputStream)r.getEntity());
	 InstalledBun output = parser.readValueAs(InstalledBun.class);
	 logger.info("InstalledServiceoutput = "+output.getUuid()+ ", status="+output.getStatus() );
	 assertEquals(InstalledBunStatus.INIT , output.getStatus() );
	
	 //wait for 2 seconds
	 Thread.sleep(2000);
	 //ask again about this task
	 client = WebClient.create(endpointUrl + "/services/baker/api/ibuns/"+uuid);
	 r = client.accept("application/json").type("application/json").get();
	
	
	 factory = new MappingJsonFactory();
	 parser = factory.createJsonParser((InputStream)r.getEntity());
	 output = parser.readValueAs(InstalledBun.class);
	
	 assertEquals(uuid, output.getUuid() );
	 assertEquals(InstalledBunStatus.FAILED , output.getStatus() );
	 assertEquals("(pending)", output.getName() );
	 }

	@Test
	public void testBakerRSInstallBunAndGetStatus() throws Exception {

		logger.info("Executing TEST = testBakerRSInstallServiceAndGetStatus");

		List<Object> providers = new ArrayList<Object>();
		providers.add(new org.codehaus.jackson.jaxrs.JacksonJsonProvider());
		String uuid = UUID.fromString("77777777-668b-4c75-99a9-39b24ed3d8be").toString();

		// first delete an existing installation if exists

		WebClient client = WebClient.create(endpointUrl + "/services/baker/api/ibuns/" + uuid, providers);
		Response r = client.accept("application/json").type("application/json").delete();
		if (Response.Status.NOT_FOUND.getStatusCode() != r.getStatus()) {
			
			assertEquals(Response.Status.OK.getStatusCode(), r.getStatus());
			logger.info("Bun is already installed! We uninstall it first!");
			int guard = 0;
			InstalledBun insbun = null;
			do {

				// ask again about this task
				client = WebClient.create(endpointUrl + "/services/baker/api/ibuns/" + uuid);
				r = client.accept("application/json").type("application/json").get();

				MappingJsonFactory factory = new MappingJsonFactory();
				JsonParser parser = factory.createJsonParser((InputStream) r.getEntity());
				insbun = parser.readValueAs(InstalledBun.class);

				logger.info("Waiting for UNINSTALLED for test bun UUID=" + uuid + " . Now is: " + insbun.getStatus());
				Thread.sleep(2000);
				guard++;

			} while ((insbun != null) && (insbun.getStatus() != InstalledBunStatus.UNINSTALLED) && (guard <= 30));

			assertEquals(InstalledBunStatus.UNINSTALLED, insbun.getStatus());

		}

		// now post a new installation
		client = WebClient.create(endpointUrl + "/services/baker/api/ibuns", providers);
		InstalledBun is = prepareInstalledService(uuid);
		r = client.accept("application/json").type("application/json").post(is);
		assertEquals(Response.Status.OK.getStatusCode(), r.getStatus());

		int guard = 0;

		InstalledBun insbun = null;
		do {

			// ask again about this task
			client = WebClient.create(endpointUrl + "/services/baker/api/ibuns/" + uuid);
			r = client.accept("application/json").type("application/json").get();

			MappingJsonFactory factory = new MappingJsonFactory();
			JsonParser parser = factory.createJsonParser((InputStream) r.getEntity());
			insbun = parser.readValueAs(InstalledBun.class);

			logger.info("Waiting for STARTED for test bun UUID=" + uuid + " . Now is: " + insbun.getStatus());
			Thread.sleep(1000);
			guard++;

		} while ((insbun != null) && (insbun.getStatus() != InstalledBunStatus.STARTED) && (insbun.getStatus() != InstalledBunStatus.FAILED) && (guard <= 30));

		assertEquals(uuid, insbun.getUuid());
		assertEquals(InstalledBunStatus.STARTED, insbun.getStatus());
		assertEquals("IntegrTestLocal example service", insbun.getName());

	}

	// helpers
	private InstalledBun prepareInstalledService(String uuid) {
		InstalledBun is = new InstalledBun();
		is.setUuid(uuid);
		is.setRepoUrl(endpointUrl + "/services/baker/repo/ibuns/" + uuid);
		return is;
	}
}
