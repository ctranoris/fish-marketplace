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
import gr.upatras.ece.nam.baker.model.BakerUser;
import gr.upatras.ece.nam.baker.model.BunMetadata;

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
public class BakerRepoTest {

	@Autowired
	private BakerJpaController bakerJpaControllerTest;

	private static final transient Log logger = LogFactory.getLog(BakerRepoTest.class.getName());

	
	@Test
	public void testWriteReadDB() {

		bakerJpaControllerTest.deleteAllBunMetadata();
		bakerJpaControllerTest.deleteAllUsers();
		
		BakerUser bu = new BakerUser();
		bu.setOrganization("UoP");
		bu.setName("aname");
		bu.setUsername("ausername");
		bu.setPassword("apassword");
		
		BunMetadata bmeta = new BunMetadata();
		bmeta.setName("abun");
		bmeta.setLongDescription("longDescription");
		bmeta.setShortDescription("shortDescription");
		bmeta.setPackageLocation("packageLocation");
		bmeta.setOwner(bu);
		bu.addBun(bmeta);


		String uuid = bmeta.getUuid();
		assertNull(uuid);
		
		bakerJpaControllerTest.saveUser(bu);
//		bakerJpaControllerTest.getAllUsersPrinted();
//		bakerJpaControllerTest.getAllBunsPrinted();
		
		uuid = bmeta.getUuid();
		assertNotNull(uuid);
		logger.info("UUID After saving= " + uuid);
		//change name and reSave
		bmeta.setName("NewBunName");
		bakerJpaControllerTest.updateBunMetadata(bmeta);
//		bakerJpaControllerTest.getAllBunsPrinted();
		
		
		bmeta = new BunMetadata();
		bmeta.setName("abun2");
		bmeta.setLongDescription("longDescription2");
		bmeta.setShortDescription("shortDescription2");
		bmeta.setPackageLocation("packageLocation2");
		bmeta.setOwner(bu);
		bu.addBun(bmeta);
		
		bakerJpaControllerTest.updateBakerUser(bu);
//		bakerJpaControllerTest.getAllBunsPrinted();
		bakerJpaControllerTest.getAllUsersPrinted();
		
		
		BakerUser testbu = bakerJpaControllerTest.readBakerUserByUsername("ausername");
		assertEquals("aname", testbu.getName());
		assertEquals("apassword", testbu.getPassword());
		assertEquals("UoP", testbu.getOrganization());
		assertEquals(2, testbu.getBuns().size() );
		

		BunMetadata testbm = bakerJpaControllerTest.readBunMetadataByUUID(uuid);
		assertEquals("NewBunName", testbm.getName());
		assertEquals(uuid, testbm.getUuid());
		assertNotNull(testbm.getOwner());
		assertEquals("ausername", testbm.getOwner().getUsername() );
		
		
		bu = new BakerUser();
		bu.setOrganization("UoP2");
		bu.setName("aname2");
		bu.setUsername("ausername2");
		bu.setPassword("apassword2");

		bakerJpaControllerTest.saveUser(bu);
		bakerJpaControllerTest.getAllUsersPrinted();
		assertEquals(2, bakerJpaControllerTest.countUsers() );
		

	}

	
}
