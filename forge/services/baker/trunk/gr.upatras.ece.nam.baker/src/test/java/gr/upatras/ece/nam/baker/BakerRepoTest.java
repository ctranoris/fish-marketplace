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
import gr.upatras.ece.nam.baker.model.ApplicationMetadata;
import gr.upatras.ece.nam.baker.model.BakerUser;
import gr.upatras.ece.nam.baker.model.BunMetadata;
import gr.upatras.ece.nam.baker.model.Category;
import gr.upatras.ece.nam.baker.model.ProductExtensionItem;
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
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
//@Transactional
public class BakerRepoTest {

	@Autowired
	private  BakerJpaController bakerJpaControllerTest;

	// private static final transient Log logger = LogFactory.getLog(BakerRepoTest.class.getName());

	@Before
	public void deletePreviousobjectsDB() {

		bakerJpaControllerTest.deleteAllProducts();
		bakerJpaControllerTest.deleteAllUsers();
		bakerJpaControllerTest.deleteAllSubscribedMachines();
		bakerJpaControllerTest.deleteAllCategories();

	}

	@Test
	public void testWriteReadDB() {

		bakerJpaControllerTest.getAllProductsPrinted();
		
		BakerUser bu = new BakerUser();
		bu.setOrganization("UoP");
		bu.setName("aname");
		bu.setUsername("ausername");
		bu.setPassword("apassword");
		bu.setEmail("e@e.com");

		bakerJpaControllerTest.saveUser(bu);
		
		BunMetadata bmeta = new BunMetadata();
		bmeta.setName("abun");
		String uuid = UUID.randomUUID().toString();
		bmeta.setUuid(uuid);
		bmeta.setLongDescription("longDescription");
		bmeta.setShortDescription("shortDescription");
		bmeta.setPackageLocation("packageLocation");
		bmeta.addExtensionItem("aname", "avalue");
		bmeta.addExtensionItem("aname", "avalue");
		bmeta.addExtensionItem("aname1", "avalue1");
		bu.addProduct(bmeta);

		bakerJpaControllerTest.updateBakerUser(bu);
		
		// change name and reSave
		bmeta = (BunMetadata) bakerJpaControllerTest.readProductByUUID(uuid);
		bmeta.setName("NewBunName");
		bakerJpaControllerTest.updateProduct(bmeta);		

		bakerJpaControllerTest.getAllProductsPrinted();
		
		bmeta = new BunMetadata();
		String uuid2 = UUID.randomUUID().toString();
		bmeta.setUuid(uuid2);
		bmeta.setName("abun2");
		bmeta.setLongDescription("longDescription2");
		bmeta.setShortDescription("shortDescription2");
		bmeta.setPackageLocation("packageLocation2");
		bu = bakerJpaControllerTest.readBakerUserByUsername("ausername");
		bu.addProduct(bmeta);

		bakerJpaControllerTest.updateBakerUser(bu);

		BakerUser testbu = bakerJpaControllerTest.readBakerUserByUsername("ausername");
		assertEquals("aname", testbu.getName());
		assertEquals(EncryptionUtil.hash("apassword"), testbu.getPassword());
		assertEquals("UoP", testbu.getOrganization());
		assertEquals("e@e.com", testbu.getEmail());


		bakerJpaControllerTest.getAllProductsPrinted();
		
		assertEquals(2, testbu.getProducts().size());

		BunMetadata testbm = (BunMetadata) bakerJpaControllerTest.readProductByUUID(uuid);
		assertEquals("NewBunName", testbm.getName());
		assertEquals(uuid, testbm.getUuid());
		assertNotNull(testbm.getOwner());
		assertEquals("ausername", testbm.getOwner().getUsername());
		assertEquals( 2, testbm.getExtensions().size() );
		assertEquals( "aname", testbm.getExtensions().get(0).getName() );
		assertEquals( "aname1", testbm.getExtensions().get(1).getName() );

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
	
	@Test
	public void testWriteReadApplications() {
		
		Category c = new Category();
		c.setName("acat1");
		assertEquals("acat1", c.getName());
		Category c2 = new Category();
		c2.setName("acat2");
		
		BakerUser bu = new BakerUser();
		bu.setUsername("ausername");

		ApplicationMetadata appmeta = new ApplicationMetadata();
		appmeta.setName("app");
		String uuid = UUID.randomUUID().toString();
		appmeta.setUuid(uuid);
		appmeta.setLongDescription("longDescription");
		appmeta.setShortDescription("shortDescription");
		appmeta.getCategories().add(c);
		appmeta.getCategories().add(c2);
		ProductExtensionItem item = new ProductExtensionItem();
		item.setName("param1");
		item.setValue("value1");
		appmeta.addExtensionItem(item );
		ProductExtensionItem item2 = new ProductExtensionItem();
		item.setName("param2");
		item.setValue("value2");
		appmeta.addExtensionItem(item2 );
		bu.addProduct(appmeta);

		bakerJpaControllerTest.saveUser(bu);

		// change name and reSave
		appmeta.setName("NewAppName");
		bakerJpaControllerTest.updateProduct(appmeta);
		assertEquals(2, appmeta.getCategories().size() );
		assertEquals(2, appmeta.getExtensions().size() );

		ApplicationMetadata appmeta2 = new ApplicationMetadata();
		appmeta2.setName("app2");
		appmeta2.setLongDescription("longDescription2");
		appmeta2.setShortDescription("shortDescription2");
		appmeta2.setOwner(bu);
		appmeta2.getCategories().add(c);
		bu.addProduct(appmeta2);

		bakerJpaControllerTest.updateBakerUser(bu);
		bakerJpaControllerTest.getAllUsersPrinted();

		BakerUser testbu = bakerJpaControllerTest.readBakerUserByUsername("ausername");
		assertEquals(2, testbu.getProducts().size());

		ApplicationMetadata testApp = (ApplicationMetadata) bakerJpaControllerTest.readProductByUUID(uuid);
		assertEquals("NewAppName", testApp.getName());
		assertEquals(uuid, testApp.getUuid());
		assertNotNull(testApp.getOwner());
		assertEquals("ausername", testApp.getOwner().getUsername());
		bakerJpaControllerTest.getAllCategoriesPrinted();
		assertEquals("acat1", testApp.getCategories().get(0).getName());


	}

}
