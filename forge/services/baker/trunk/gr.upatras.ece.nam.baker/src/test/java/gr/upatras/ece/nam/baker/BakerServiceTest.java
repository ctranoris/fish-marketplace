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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
	public void testReqInstall_toSTARTEDStatus() {
		BakerService bs = BakerServiceInit();
		bs.setRepoWebClient( new MockRepositoryWebClient( "NORMAL" )  );
				
		UUID uuid = UUID.randomUUID();
		//we don;t care about repo...we provide a local package hardcoded by MockRepositoryWebClient
		InstalledService is = bs.installService(uuid,  "www.repoexample.com/repo/EBUNID/"+uuid);
		assertNotNull(is);
		assertEquals( 1 , bs.getManagedServices().size());
		assertEquals( is.getStatus(),  InstalledServiceStatus.INIT );		

		logger.info(" test service UUID="+uuid+" . Now is: "+ is.getStatus());
		
		while (  (is.getStatus()  != InstalledServiceStatus.STARTED) &&
				is.getStatus()  != InstalledServiceStatus.FAILED){
			logger.info("Waiting for STARTED for test service UUID="+uuid+" . Now is: "+ is.getStatus());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}		
		
		InstalledService istest = bs.getService(uuid);
		assertNotNull(istest);
		assertNotNull( istest.getServiceMetadata() );
		assertEquals(uuid, istest.getUuid());
		assertEquals(is.getUuid(), istest.getUuid());
		assertEquals( InstalledServiceStatus.STARTED, istest.getStatus() );	
		assertEquals("www.repoexample.com/repo/EBUNID/"+uuid, istest.getRepoUrl());
		assertEquals("/files/examplebun.tar.gz", istest.getServiceMetadata().getPackageLocation() );
		assertEquals("TemporaryServiceFromMockClass", istest.getServiceMetadata().getName() );
		assertEquals("1.0.0.test", istest.getServiceMetadata().getVersion() );
		assertEquals("TemporaryServiceFromMockClass", istest.getName());
		assertEquals("1.0.0.test", istest.getInstalledVersion() );
		assertEquals( 1 , bs.getManagedServices().size());
		
		
		
	}

	@Test
	public void testReqInstall_ErrScript() {
		BakerService bs = BakerServiceInit();
		bs.setRepoWebClient( new MockRepositoryWebClient( "NORMAL" )  );
				
		UUID uuid = UUID.randomUUID();
		//we don;t care about repo...we provide a local package hardcoded by MockRepositoryWebClient
		InstalledService is = bs.installService(uuid,  "www.repoexample.com/repo/EBUNERR/"+uuid);
		assertNotNull(is);
		assertEquals( 1 , bs.getManagedServices().size());
		assertEquals( is.getStatus(),  InstalledServiceStatus.INIT );		

		logger.info(" test service UUID="+uuid+" . Now is: "+ is.getStatus());
		
		while (  (is.getStatus()  != InstalledServiceStatus.STARTED) &&
				is.getStatus()  != InstalledServiceStatus.FAILED){
			logger.info("Waiting for STARTED for test service UUID="+uuid+" . Now is: "+ is.getStatus());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}		
		
		InstalledService istest = bs.getService(uuid);
		assertNotNull(istest);
		assertNotNull( istest.getServiceMetadata() );
		assertEquals(uuid, istest.getUuid());
		assertEquals(is.getUuid(), istest.getUuid());
		assertEquals( InstalledServiceStatus.FAILED, istest.getStatus() );	
		assertEquals("www.repoexample.com/repo/EBUNERR/"+uuid, istest.getRepoUrl());
		assertEquals("/files/examplebunErrInstall.tar.gz", istest.getServiceMetadata().getPackageLocation() );
		assertEquals("TemporaryServiceFromMockClass", istest.getServiceMetadata().getName() );
		assertEquals("1.0.0.test", istest.getServiceMetadata().getVersion() );
		assertEquals("(pending)", istest.getName());
		assertNull(  istest.getInstalledVersion() );
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
