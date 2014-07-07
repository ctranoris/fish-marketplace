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
import gr.upatras.ece.nam.baker.impl.BakerJpaController;
import gr.upatras.ece.nam.baker.model.BakerService;
import gr.upatras.ece.nam.baker.model.InstalledService;
import gr.upatras.ece.nam.baker.model.InstalledServiceStatus;
import gr.upatras.ece.nam.baker.model.ServiceMetadata;
import gr.upatras.ece.nam.baker.testclasses.MockRepositoryWebClient;

import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:contextTest.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional
public class BakerServiceTest {

	@Autowired
	private BakerJpaController bakerJpaControllerTest;

	private static final transient Log logger = LogFactory.getLog(BakerServiceTest.class.getName());

	@Test
	public void testGetManagedServices() {
		BakerService bs = BakerServiceInit(new MockRepositoryWebClient("NORMAL"), bakerJpaControllerTest);
		assertNotNull(bs.getManagedServices());
		logger.info("	 	>>>>	bakerJpaControllerTest = " + bakerJpaControllerTest);
	}

	@Test
	public void TestWriteReadDB() {

		bakerJpaControllerTest.deleteAllInstalledService();

		String uuid = UUID.randomUUID().toString();
		InstalledService istest = new InstalledService(uuid, "www.repoexample.com/repo/EBUNID/" + uuid);
		istest.setInstalledVersion("1.0.0v");
		istest.setName("NONMAE");
		istest.setStatus(InstalledServiceStatus.INSTALLING);

		bakerJpaControllerTest.saveInstalledService(istest);
		//bakerJpaControllerTest.getAll();

		InstalledService retIs = bakerJpaControllerTest.readInstalledServiceByUUID(uuid);
		assertEquals(uuid, retIs.getUuid());
		assertEquals(InstalledServiceStatus.INSTALLING, retIs.getStatus());
		assertEquals("NONMAE", retIs.getName());
		assertEquals(1, bakerJpaControllerTest.countInstalledServices());

		// second one with metadata
		uuid = UUID.randomUUID().toString();
		istest = new InstalledService(uuid, "www.repoexample.com/repo/EBUNID/" + uuid);
		istest.setInstalledVersion("1.0.0v");
		istest.setName("NONMAE2");
		istest.setStatus(InstalledServiceStatus.STARTING);
		ServiceMetadata sm = new ServiceMetadata();
		sm.setUuid(uuid);
		sm.setName("MetadataName");
		sm.setPackageLocation("/repo/aaa.tar.gz");
		istest.setServiceMetadata(sm);
		bakerJpaControllerTest.saveInstalledService(istest);
		//bakerJpaControllerTest.getAll();
		retIs = bakerJpaControllerTest.readInstalledServiceByUUID(uuid);
		assertEquals(uuid, retIs.getUuid());
		assertEquals(InstalledServiceStatus.STARTING, retIs.getStatus());
		assertEquals("NONMAE2", retIs.getName());
		assertEquals("/repo/aaa.tar.gz", retIs.getServiceMetadata().getPackageLocation());
		assertEquals("MetadataName", retIs.getServiceMetadata().getName());
		assertEquals(2, bakerJpaControllerTest.countInstalledServices());

		// update it
		istest.setStatus(InstalledServiceStatus.STARTED);
		bakerJpaControllerTest.update(istest);
		retIs = bakerJpaControllerTest.readInstalledServiceByUUID(uuid);
		assertEquals(InstalledServiceStatus.STARTED, retIs.getStatus());
		//bakerJpaControllerTest.getAll();
		assertEquals(2, bakerJpaControllerTest.countInstalledServices());

	}

	@Test
	public void testReqInstall_toSTARTEDStatus() {
		bakerJpaControllerTest.deleteAllInstalledService();
		BakerService bs = BakerServiceInit(new MockRepositoryWebClient("NORMAL"), bakerJpaControllerTest);
		bs.setRepoWebClient(new MockRepositoryWebClient("NORMAL"));
		

		String uuid = UUID.randomUUID().toString();
		// we don;t care about repo...we provide a local package hardcoded by MockRepositoryWebClient
		InstalledService is = bs.installService(uuid, "www.repoexample.com/repo/EBUNID/" + uuid);
		assertNotNull(is);
		assertEquals(1, bs.getManagedServices().size());
		assertEquals(is.getStatus(), InstalledServiceStatus.INIT);

		logger.info(" test service UUID=" + uuid + " . Now is: " + is.getStatus());

		int guard = 0;
		while ((is.getStatus() != InstalledServiceStatus.STARTED) && (is.getStatus() != InstalledServiceStatus.FAILED) && (guard <= 30)) {
			logger.info("Waiting for STARTED for test service UUID=" + uuid + " . Now is: " + is.getStatus());
			try {
				Thread.sleep(1000);
				guard++;
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}

		InstalledService istest = bs.getService(uuid);
		assertNotNull(istest);
		assertNotNull(istest.getServiceMetadata());
		assertEquals(uuid, istest.getUuid());
		assertEquals(is.getUuid(), istest.getUuid());
		assertEquals(InstalledServiceStatus.STARTED, istest.getStatus());
		assertEquals("www.repoexample.com/repo/EBUNID/" + uuid, istest.getRepoUrl());
		assertEquals("/files/examplebun.tar.gz", istest.getServiceMetadata().getPackageLocation());
		assertEquals("TemporaryServiceFromMockClass", istest.getServiceMetadata().getName());
		assertEquals("1.0.0.test", istest.getServiceMetadata().getVersion());
		assertEquals("TemporaryServiceFromMockClass", istest.getName());
		assertEquals("1.0.0.test", istest.getInstalledVersion());
		assertEquals(1, bs.getManagedServices().size());

	}

	@Test
	public void testReqInstall_ErrScript() {
		bakerJpaControllerTest.deleteAllInstalledService();
		BakerService bs = BakerServiceInit(new MockRepositoryWebClient("NORMAL"), bakerJpaControllerTest);
		bs.setRepoWebClient(new MockRepositoryWebClient("NORMAL"));

		String uuid = UUID.randomUUID().toString();
		// we don;t care about repo...we provide a local package hardcoded by MockRepositoryWebClient
		InstalledService is = bs.installService(uuid, "www.repoexample.com/repo/EBUNERR/" + uuid);
		assertNotNull(is);
		assertEquals(1, bs.getManagedServices().size());
		assertEquals(is.getStatus(), InstalledServiceStatus.INIT);

		logger.info(" test service UUID=" + uuid + " . Now is: " + is.getStatus());

		int guard = 0;
		while ((is.getStatus() != InstalledServiceStatus.STARTED) && (is.getStatus() != InstalledServiceStatus.FAILED) && (guard <= 30)) {
			logger.info("Waiting for STARTED for test service UUID=" + uuid + " . Now is: " + is.getStatus());
			try {
				Thread.sleep(1000);
				guard++;
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}

		InstalledService istest = bs.getService(uuid);
		assertNotNull(istest);
		assertNotNull(istest.getServiceMetadata());
		assertEquals(uuid, istest.getUuid());
		assertEquals(is.getUuid(), istest.getUuid());
		assertEquals(InstalledServiceStatus.FAILED, istest.getStatus());
		assertEquals("www.repoexample.com/repo/EBUNERR/" + uuid, istest.getRepoUrl());
		assertEquals("/files/examplebunErrInstall.tar.gz", istest.getServiceMetadata().getPackageLocation());
		assertEquals("TemporaryServiceFromMockClass", istest.getServiceMetadata().getName());
		assertEquals("1.0.0.test", istest.getServiceMetadata().getVersion());
		assertEquals("(pending)", istest.getName());
		assertNull(istest.getInstalledVersion());
		assertEquals(1, bs.getManagedServices().size());

	}

	@Test
	public void testReqInstall_AndPersistence() {

		bakerJpaControllerTest.deleteAllInstalledService();
		BakerService bs = BakerServiceInit(new MockRepositoryWebClient("NORMAL"), bakerJpaControllerTest);

		String uuid = UUID.randomUUID().toString();
		// we don;t care about repo...we provide a local package hardcoded by MockRepositoryWebClient
		InstalledService is = bs.installService(uuid, "www.repoexample.com/repo/EBUNID/" + uuid);
		assertNotNull(is);
		assertEquals(1, bs.getManagedServices().size());
		assertEquals(is.getStatus(), InstalledServiceStatus.INIT);
		assertEquals(1, bakerJpaControllerTest.countInstalledServices());

		// bakerJpaControllerTest.getAll();

		int guard = 0;
		while ((is.getStatus() != InstalledServiceStatus.STARTED) && (is.getStatus() != InstalledServiceStatus.FAILED) && (guard <= 30)) {
			logger.info("Waiting for STARTED for test service UUID=" + uuid + " . Now is: " + is.getStatus());
			try {
				Thread.sleep(1000);
				guard++;
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}

		InstalledService istest = bs.getService(uuid);
		assertNotNull(istest.getServiceMetadata());
		assertEquals(uuid, istest.getUuid());
		assertEquals(InstalledServiceStatus.STARTED, istest.getStatus());
		assertEquals(1, bs.getManagedServices().size());
		InstalledService retIs = bakerJpaControllerTest.readInstalledServiceByUUID(istest.getUuid());
		assertEquals(InstalledServiceStatus.STARTED, retIs.getStatus());

		bs = null; // remove the old one

		// create new one..It should persist any installed service
		BakerService bsNew = BakerServiceInit(new MockRepositoryWebClient("NORMAL"), bakerJpaControllerTest);
		// bakerJpaControllerTest.getAll();

		assertEquals("Persistence not implemented yet?!?", 1, bsNew.getManagedServices().size());// there should be one
		InstalledService istestNew = bsNew.getService(uuid); // req the service with the previous uuid
		assertNotNull(istest.getServiceMetadata());
		assertEquals(uuid, istestNew.getUuid());
		assertEquals(InstalledServiceStatus.STARTED, istestNew.getStatus());

	}

	// helper functions

	public BakerService BakerServiceInit(MockRepositoryWebClient mockRepositoryWebClient, BakerJpaController bakerJpaControllerTest2) {

		
		BakerService bs = new BakerService();
		bs.setRepoWebClient(new MockRepositoryWebClient("NORMAL"));
		bs.setBakerJpaController( bakerJpaControllerTest);
		return bs;
	}
}
