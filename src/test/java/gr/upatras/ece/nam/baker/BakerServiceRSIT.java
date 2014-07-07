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
import gr.upatras.ece.nam.baker.model.InstalledService;
import gr.upatras.ece.nam.baker.model.InstalledServiceStatus;

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

public class BakerServiceRSIT {

	private static String endpointUrl;
	private static final transient Log logger = LogFactory.getLog(BakerServiceRSIT.class.getName());

    @BeforeClass
    public static void beforeClass() {
        endpointUrl = System.getProperty("service.url");
    }

    @Test
    public void testBakerRSInstallServiceNotFoundAnFail() throws Exception {
    	logger.info("testBakerRSInstallServiceNotFound");
        List<Object> providers = new ArrayList<Object>();
        providers.add(new org.codehaus.jackson.jaxrs.JacksonJsonProvider());
        UUID uuid = UUID.fromString("55cab8b8-668b-4c75-99a9-39b24ed3d8be");
        InstalledService is = prepeareInstalledService(uuid.toString());
                
        WebClient client = WebClient.create(endpointUrl + "/services/baker/api/iservices/", providers);
        Response r = client.accept("application/json")
            .type("application/json")
            .post(is);
        assertEquals(Response.Status.OK.getStatusCode(), r.getStatus());

        MappingJsonFactory factory = new MappingJsonFactory();
        JsonParser parser = factory.createJsonParser((InputStream)r.getEntity());
        InstalledService output = parser.readValueAs(InstalledService.class);
        assertEquals(InstalledServiceStatus.INIT , output.getStatus()  );      
        
        //wait for 2 seconds
        Thread.sleep(2000);
        //ask again about this task
        client = WebClient.create(endpointUrl + "/services/baker/api/iservices/"+uuid);
        r = client.accept("application/json").type("application/json").get();
        
        
        factory = new MappingJsonFactory();
        parser = factory.createJsonParser((InputStream)r.getEntity());
        output = parser.readValueAs(InstalledService.class);

        assertEquals(uuid, output.getUuid() );
        assertEquals(InstalledServiceStatus.FAILED , output.getStatus()  );
        assertEquals("(pending)", output.getName() );
    }
    
    @Test
    public void testBakerRSInstallServiceAndGetStatus() throws Exception {
    	logger.info("testBakerRSInstallServiceAndGetStatus");
    	
        List<Object> providers = new ArrayList<Object>();
        providers.add(new org.codehaus.jackson.jaxrs.JacksonJsonProvider());
        UUID uuid = UUID.fromString("12cab8b8-668b-4c75-99a9-39b24ed3d8be");
        InstalledService is = prepeareInstalledService(uuid.toString());
                
        WebClient client = WebClient.create(endpointUrl + "/services/baker/api/iservices", providers);
        //first post a new installation
        Response r = client.accept("application/json")
            .type("application/json")
            .post(is);
        assertEquals(Response.Status.OK.getStatusCode(), r.getStatus());
        
        //wait for 12 seconds
        Thread.sleep(12000);
        
        //ask again about this task
        client = WebClient.create(endpointUrl + "/services/baker/api/iservices/"+uuid);
        r = client.accept("application/json").type("application/json").get();
        
        MappingJsonFactory factory = new MappingJsonFactory();
        JsonParser parser = factory.createJsonParser((InputStream)r.getEntity());
        InstalledService output = parser.readValueAs(InstalledService.class);

        assertEquals(uuid, output.getUuid() );
        assertEquals(InstalledServiceStatus.STARTED , output.getStatus()  );
        assertEquals("Local example service", output.getName() );
        
        
    }
    
    //helpers
    private InstalledService prepeareInstalledService(String uuid){
    	InstalledService is = new InstalledService( );     
        is.setUuid(uuid);
        is.setRepoUrl( endpointUrl +"/services/baker/localrepo/iservices/"+uuid);
        return is;
    }
}
