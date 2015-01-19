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
import gr.upatras.ece.nam.baker.model.Container;
import gr.upatras.ece.nam.baker.model.DeployArtifact;
import gr.upatras.ece.nam.baker.model.DeployContainer;
import gr.upatras.ece.nam.baker.model.DeploymentDescriptor;
import gr.upatras.ece.nam.baker.model.DeploymentDescriptorStatus;
import gr.upatras.ece.nam.baker.model.ProductExtensionItem;
import gr.upatras.ece.nam.baker.model.SubscribedResource;
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
		bakerJpaControllerTest.deleteAllSubscribedResources();
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
	public void testSubscribedResources() {
		SubscribedResource sm = new SubscribedResource();
		sm.setURL("testURL");

		assertEquals("testURL", sm.getURL());

		bakerJpaControllerTest.saveSubscribedResource(sm);

		sm.setURL("testURL1");
		bakerJpaControllerTest.updateSubscribedResource(sm);

		SubscribedResource testsm = bakerJpaControllerTest.readSubscribedResourceById(sm.getId());
		assertEquals("testURL1", testsm.getURL());

		sm = new SubscribedResource();
		sm.setURL("anotherTestURL");
		bakerJpaControllerTest.saveSubscribedResource(sm);
		bakerJpaControllerTest.getAllSubscribedResourcesPrinted();
		assertEquals(2, bakerJpaControllerTest.countSubscribedResources());

		bakerJpaControllerTest.deleteSubscribedResource(sm.getId());

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
	
	@Test
	public void testDeployDescriptorApplications() {
		Category c = new Category();
		c.setName("acat1");
		BakerUser bu = new BakerUser();
		bu.setUsername("ausername");
		
		//add a couple of buns
		BunMetadata bmeta = new BunMetadata();
		bmeta.setName("bun1");
		String uuid = UUID.randomUUID().toString();
		bmeta.setUuid(uuid);
		bmeta.addExtensionItem("aname1", "avalue1");
		bmeta.addExtensionItem("aname2", "avalue2");
		bu.addProduct(bmeta);
		
		BunMetadata bmeta2 = new BunMetadata();
		bmeta2.setName("bun2");
		uuid = UUID.randomUUID().toString();
		bmeta2.setUuid(uuid);
		bmeta2.addExtensionItem("aname11", "avalue11");
		bmeta2.addExtensionItem("aname21", "avalue21");
		bu.addProduct(bmeta2);		
		
		//add an application description
		ApplicationMetadata app = new ApplicationMetadata();
		app.setName("myapp");
		uuid = UUID.randomUUID().toString();
		app.setUuid(uuid);
		app.setLongDescription("longDescription");
		app.setShortDescription("shortDescription");
		app.getCategories().add(c);
		Container container = new Container(); //add a container
		container.setName("Container0");		
		DeployArtifact deployArtifact = new DeployArtifact();
		deployArtifact.setName(bmeta2.getName() );
		deployArtifact.setUuid(bmeta2.getUuid());
		container.getDeployArtifacts().add(deployArtifact);
		app.getContainers().add(container );		
		bu.addProduct(app);
		
		//now create a dployment
		DeploymentDescriptor dd = new DeploymentDescriptor();
		dd.setBaseApplication(app);
		dd.setName("a test DeployDescriptor");
		dd.setOwner(bu);
		dd.setStatus( DeploymentDescriptorStatus.PENDING_ADMIN_AUTH );
		DeployContainer deplContainer = new DeployContainer();
		deplContainer.setName("deploy1");
		DeployArtifact deployArtifactInst = new DeployArtifact();
		deployArtifactInst.setName( dd.getBaseApplication().getContainers().get(0).getDeployArtifacts().get(0).getName() );
		deployArtifactInst.setUuid( dd.getBaseApplication().getContainers().get(0).getDeployArtifacts().get(0).getUuid() );		
		deplContainer.getDeployArtifacts().add(deployArtifactInst);
		SubscribedResource targetResource = new SubscribedResource();
		targetResource.setURL("targetIP");
		deplContainer.setTargetResource(targetResource );
		dd.getDeployContainers().add(deplContainer);
		bu.getDeployments().add(dd);//now add the deployment to the user

		bakerJpaControllerTest.saveUser(bu);
		

		BakerUser testbu = bakerJpaControllerTest.readBakerUserByUsername("ausername") ;		

		assertEquals("myapp", testbu.getDeployments().get(0).getBaseApplication().getName() );
		assertEquals("targetIP", testbu.getDeployments().get(0).getDeployContainers().get(0).getTargetResource().getURL()  );
		
	}

}
