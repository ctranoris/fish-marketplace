package gr.upatras.ece.nam.baker;

import static org.junit.Assert.*;

import java.util.UUID;

import gr.upatras.ece.nam.baker.model.BakerService;
import gr.upatras.ece.nam.baker.model.InstalledService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class BakerServiceTest {


	private static final transient Log logger = LogFactory.getLog(BakerServiceTest.class.getName());
	
	@Test
	public void testGetManagedServices() {
		BakerService bs = BakerServiceInit();
		assertNotNull( bs .getManagedServices());
	}


	@Test
	public void testGetService() {
		BakerService bs = BakerServiceInit();
		UUID uuid = UUID.randomUUID();
		InstalledService is = bs.installService(uuid,  "www.repoexample.com", "1.1.1");
		assertNotNull(is);
		
		InstalledService istest = bs.getService(uuid);
		assertNotNull(istest);
		assertEquals(uuid, istest.getUuid());
		assertEquals(is.getUuid(), istest.getUuid());
		assertEquals("www.repoexample.com", istest.getRepoUrl());
		assertEquals("(pending)", istest.getName());
		assertEquals("1.1.1", istest.getInstalledVersion());
	}

	@Test
	public void testInstallService() {
		BakerService bs = BakerServiceInit();
		UUID uuid = UUID.randomUUID();
		InstalledService is = bs.installService(uuid,  "www.repoexample.com", "1.1.1");
		for (int i = 0; i < 20; i++) { //add 20 more random
			bs.installService( UUID.randomUUID() ,  "www.repoexample.comRANDOM", "1.1.1RANDOM"+i);
		}
		assertNotNull(is);
		assertEquals(uuid, is.getUuid());
		assertEquals(21, bs.getManagedServices().size() );
	}

	@Test
	public void testUninstallService() {
		BakerService bs = BakerServiceInit();
		UUID uuid = UUID.randomUUID();
		InstalledService is = bs.installService(uuid, "www.repoexample.com", "1.1.1");
		for (int i = 0; i < 20; i++) { //add 20 more random
			bs.installService( UUID.randomUUID() , "www.repoexample.comRANDOM", "1.1.1RANDOM"+i);
		}
		assertNotNull(is);
		assertEquals(uuid, is.getUuid());
		assertEquals(21, bs.getManagedServices().size() );
		
		Boolean res = bs.uninstallService(uuid);		

		assertEquals( true, res );
		assertEquals(20, bs.getManagedServices().size() );
		
		
	}

	//helper functions
	
	public BakerService BakerServiceInit(){
		BakerService bs = new BakerService();
		return bs;
	}
}
