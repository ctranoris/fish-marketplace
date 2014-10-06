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

import java.util.UUID;

import gr.upatras.ece.nam.baker.impl.BakerJpaController;
import gr.upatras.ece.nam.baker.model.BakerUser;
import gr.upatras.ece.nam.baker.model.BunMetadata;
import gr.upatras.ece.nam.baker.model.SubscribedMachine;
import gr.upatras.ece.nam.baker.util.EncryptionUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.BeforeClass;
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
public class BakerRepoTest {

	@Autowired
	private  BakerJpaController bakerJpaControllerTest;

	// private static final transient Log logger = LogFactory.getLog(BakerRepoTest.class.getName());

	@Before
	public void deletePreviousobjectsDB() {

		bakerJpaControllerTest.deleteAllBunMetadata();
		bakerJpaControllerTest.deleteAllUsers();
		bakerJpaControllerTest.deleteAllSubscribedMachines();

	}

	@Test
	public void testWriteReadDB() {

		BakerUser bu = new BakerUser();
		bu.setOrganization("UoP");
		bu.setName("aname");
		bu.setUsername("ausername");
		bu.setPassword("apassword");
		bu.setEmail("e@e.com");

		BunMetadata bmeta = new BunMetadata();
		bmeta.setName("abun");
		String uuid = UUID.randomUUID().toString();
		bmeta.setUuid(uuid);
		bmeta.setLongDescription("longDescription");
		bmeta.setShortDescription("shortDescription");
		bmeta.setPackageLocation("packageLocation");
		bmeta.setOwner(bu);
		bu.addBun(bmeta);

		bakerJpaControllerTest.saveUser(bu);

		// change name and reSave
		bmeta.setName("NewBunName");
		bakerJpaControllerTest.updateBunMetadata(bmeta);
		// bakerJpaControllerTest.getAllBunsPrinted();

		bmeta = new BunMetadata();
		bmeta.setName("abun2");
		bmeta.setLongDescription("longDescription2");
		bmeta.setShortDescription("shortDescription2");
		bmeta.setPackageLocation("packageLocation2");
		bmeta.setOwner(bu);
		bu.addBun(bmeta);

		bakerJpaControllerTest.updateBakerUser(bu);
		// bakerJpaControllerTest.getAllBunsPrinted();
		bakerJpaControllerTest.getAllUsersPrinted();

		BakerUser testbu = bakerJpaControllerTest.readBakerUserByUsername("ausername");
		assertEquals("aname", testbu.getName());
		assertEquals(EncryptionUtil.hash("apassword"), testbu.getPassword());
		assertEquals("UoP", testbu.getOrganization());
		assertEquals("e@e.com", testbu.getEmail());

		assertEquals(2, testbu.getBuns().size());

		BunMetadata testbm = bakerJpaControllerTest.readBunMetadataByUUID(uuid);
		assertEquals("NewBunName", testbm.getName());
		assertEquals(uuid, testbm.getUuid());
		assertNotNull(testbm.getOwner());
		assertEquals("ausername", testbm.getOwner().getUsername());

		bu = new BakerUser();
		bu.setOrganization("UoP2");
		bu.setName("aname2");
		bu.setUsername("ausername2");
		bu.setPassword("apassword2");

		bakerJpaControllerTest.saveUser(bu);
		bakerJpaControllerTest.getAllUsersPrinted();
		assertEquals(2, bakerJpaControllerTest.countUsers());

	}

	@Test
	public void testSubscribedMachines() {
		SubscribedMachine sm = new SubscribedMachine();
		sm.setURL("testURL");

		assertEquals("testURL", sm.getURL());

		bakerJpaControllerTest.saveSubscribedMachine(sm);

		sm.setURL("testURL1");
		bakerJpaControllerTest.updateSubscribedMachine(sm);

		SubscribedMachine testsm = bakerJpaControllerTest.readSubscribedMachineById(sm.getId());
		assertEquals("testURL1", testsm.getURL());

		sm = new SubscribedMachine();
		sm.setURL("anotherTestURL");
		bakerJpaControllerTest.saveSubscribedMachine(sm);
		bakerJpaControllerTest.getAllSubscribedMachinesPrinted();
		assertEquals(2, bakerJpaControllerTest.countSubscribedMachines());

		bakerJpaControllerTest.deleteSubscribedMachine(sm.getId());

	}

}
