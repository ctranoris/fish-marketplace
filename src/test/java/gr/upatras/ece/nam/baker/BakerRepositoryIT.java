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

import static org.junit.Assert.assertEquals;
import gr.upatras.ece.nam.baker.model.BakerUser;
import gr.upatras.ece.nam.baker.model.BunMetadata;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.BeforeClass;
import org.junit.Test;


//RUN a single Integration Test only, but runs all unit tests
//mvn clean -Pjetty.integration -Dit.test=BakerRepositoryIT verify

public class BakerRepositoryIT {

	private static String endpointUrl;
	private static final transient Log logger = LogFactory.getLog(BakerRepositoryIT.class.getName());

	@BeforeClass
	public static void beforeClass() {
		endpointUrl = System.getProperty("service.url");
		logger.info("EbeforeClass endpointUrl = " + endpointUrl);
	}
	
	@Test
	public void testGetUsers() throws Exception {

		logger.info("Executing TEST = testGetUsers");
		List<Object> providers = new ArrayList<Object>();
		providers.add(new org.codehaus.jackson.jaxrs.JacksonJsonProvider());


		WebClient client = WebClient.create(endpointUrl + "/services/api/repo/users", providers);
		Response r = client.accept("application/json").type("application/json").get(); 
		assertEquals(Response.Status.OK.getStatusCode(), r.getStatus());		

		String bakerAPIVersionListHeaders = (String) r.getHeaders().getFirst("X-Baker-API-Version");
		assertEquals("1.0.0", bakerAPIVersionListHeaders);
		
		MappingJsonFactory factory = new MappingJsonFactory();
		JsonParser parser = factory.createJsonParser((InputStream) r.getEntity());
		

		//Collection<BakerUser> users = parser.readValueAs(Collection.class);		
		
		JsonNode node = parser.readValueAsTree();
        //node = node.get("someArray");
		 ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<BakerUser>> typeRef = new TypeReference<List<BakerUser>>(){};
        List<BakerUser> list = mapper.readValue(node.traverse(), typeRef);
        for (BakerUser f : list) {
        	logger.info("user = " + f.getName());
        }
        

//		logger.info("users = " + users.size());
//		
//		for (Iterator<BakerUser> iterator = users.iterator(); iterator.hasNext();) {
//			BakerUser bu = iterator.next();
//			logger.info("	======> BakerUser found: " + bu.getName() + ", Id: " + bu.getId() + ", Id: " + bu.getOrganization() + ", Id: " + bu.getUsername());
//
//			List<BunMetadata> buns = bu.getBuns();
//			for (BunMetadata bunMetadata : buns) {
//
//				logger.info("	======> bunMetadata found: " + bunMetadata.getName() + ", Id: " + bunMetadata.getId() + ", getUuid: " + bunMetadata.getUuid()
//						+ ", getName: " + bunMetadata.getName());
//			}
//
//		}
//		logger.info("================= getAll() ==================END");
		
		
	}
	

}
