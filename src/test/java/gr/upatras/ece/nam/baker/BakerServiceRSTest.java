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


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;



@ContextConfiguration(locations = "classpath:web_test.xml")
public class BakerServiceRSTest {

	//To create a rest test with no integration, see CXF itself source code
	//http://svn.apache.org/repos/asf/cxf/trunk/systests/jaxrs/src/test/java/org/apache/cxf/systest/jaxrs/BookServerResourceCreatedSpringProviders.java
	
	
	private static String endpointUrl;
	private static final transient Log logger = LogFactory.getLog(BakerServiceRSTest.class.getName());

   //@BeforeClass
    public static void beforeClass() {
        endpointUrl = System.getProperty("service.url");
    }

    //@Test
    public void testBakerRSInstallService() throws Exception {
    	logger.info("testBakerRSInstallService endpointUrl = "+endpointUrl);
        List<Object> providers = new ArrayList<Object>();
        providers.add(new org.codehaus.jackson.jaxrs.JacksonJsonProvider());
        UUID uuid = UUID.randomUUID();
        InstalledService is = prepeareInstalledService(uuid);
                
        WebClient client = WebClient.create(endpointUrl + "/baker/iservices", providers);
        Response r = client.accept("application/json")
            .type("application/json")
            .post(is);
        assertEquals(Response.Status.OK.getStatusCode(), r.getStatus());
        MappingJsonFactory factory = new MappingJsonFactory();
        JsonParser parser = factory.createJsonParser((InputStream)r.getEntity());
        InstalledService output = parser.readValueAs(InstalledService.class);

        assertEquals(uuid, output.getUuid() );
        assertEquals(InstalledServiceStatus.INIT , output.getStatus()  );
        assertEquals("(pending)", output.getName() );
    }
        
    //helpers
    private InstalledService prepeareInstalledService(UUID uuid){
    	InstalledService is = new InstalledService( );     
        is.setUuid(uuid);
        is.setRepoUrl( "www.ExampleRepoUrl.com/example");
        is.setInstalledVersion("1.2.3 rc1");
        return is;
    }
}
