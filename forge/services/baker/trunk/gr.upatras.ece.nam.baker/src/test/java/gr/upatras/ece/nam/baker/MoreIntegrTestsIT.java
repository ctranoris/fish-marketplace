package gr.upatras.ece.nam.baker;

import org.junit.BeforeClass;
import org.junit.Test;

public class MoreIntegrTestsIT {
	private static String endpointUrl;

    @BeforeClass
    public static void beforeClass() {
        endpointUrl = System.getProperty("service.url");
    }

    @Test
    public void testPing() throws Exception {
    	System.out.println("=============== Executing testPing =============== ");
    }
}
