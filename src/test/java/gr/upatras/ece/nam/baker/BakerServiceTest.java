package gr.upatras.ece.nam.baker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import gr.upatras.ece.nam.baker.model.BakerService;
import gr.upatras.ece.nam.baker.model.InstalledService;
import gr.upatras.ece.nam.baker.model.InstalledServiceStatus;
import gr.upatras.ece.nam.baker.testclasses.MockRepositoryWebClient;

import java.util.UUID;

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
	public void testReqInstall_toINSTALLEDStatus() {
		BakerService bs = BakerServiceInit();
		bs.setRepoWebClient( new MockRepositoryWebClient( "NORMAL" )  );
		
		UUID uuid = UUID.randomUUID();
		InstalledService is = bs.installService(uuid,  "www.repoexample.com/repo");
		assertNotNull(is);
		assertEquals( 1 , bs.getManagedServices().size());
		assertEquals( is.getStatus(),  InstalledServiceStatus.INIT );		

		
		while (  (is.getStatus()  != InstalledServiceStatus.INSTALLED) &&
				is.getStatus()  != InstalledServiceStatus.FAILED){
			logger.info("Waiting for INSTALLED for test service UUID="+uuid+" .Now is="+ is.getStatus());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		
		InstalledService istest = bs.getService(uuid);
		assertNotNull(istest);
		assertNotNull( istest.getServiceMetadata() );
		assertEquals(uuid, istest.getUuid());
		assertEquals(is.getUuid(), istest.getUuid());
		assertEquals( istest.getStatus(),  InstalledServiceStatus.INSTALLED );	
		assertEquals("www.repoexample.com/repo", istest.getRepoUrl());
		assertEquals("TemporaryServiceFromMockClass", istest.getServiceMetadata().getName() );
		assertEquals("1.0.0.test", istest.getServiceMetadata().getVersion() );
		assertEquals("TemporaryServiceFromMockClass", istest.getName());
		assertEquals("1.0.0.test", istest.getInstalledVersion() );
		assertEquals( 1 , bs.getManagedServices().size());
		
		
		
	}

//	@Test
//	public void testInstallService() {
//		BakerService bs = BakerServiceInit();
//		UUID uuid = UUID.randomUUID();
//		InstalledService is = bs.installService(uuid,  "www.repoexample.com");
//		for (int i = 0; i < 20; i++) { //add 20 more random
//			bs.installService( UUID.randomUUID() ,  "www.repoexample.comRANDOM");
//		}
//		assertNotNull(is);
//		assertEquals(uuid, is.getUuid());
//		assertEquals(21, bs.getManagedServices().size() );
//	}
//
//	@Test
//	public void testUninstallService() {
//		BakerService bs = BakerServiceInit();
//		UUID uuid = UUID.randomUUID();
//		InstalledService is = bs.installService(uuid, "www.repoexample.com");
//		for (int i = 0; i < 20; i++) { //add 20 more random
//			bs.installService( UUID.randomUUID() , "www.repoexample.comRANDOM");
//		}
//		assertNotNull(is);
//		assertEquals(uuid, is.getUuid());
//		assertEquals(21, bs.getManagedServices().size() );
//		
//		Boolean res = bs.uninstallService(uuid);		
//
//		assertEquals( true, res );
//		assertEquals(20, bs.getManagedServices().size() );
//		
//		
//	}

	//helper functions
	
	public BakerService BakerServiceInit(){
		BakerService bs = new BakerService();
		return bs;
	}
}
