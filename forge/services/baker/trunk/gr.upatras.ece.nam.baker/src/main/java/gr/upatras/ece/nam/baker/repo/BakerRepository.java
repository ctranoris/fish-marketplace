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

package gr.upatras.ece.nam.baker.repo;

import gr.upatras.ece.nam.baker.impl.BakerJpaController;
import gr.upatras.ece.nam.baker.model.ApplicationMetadata;
import gr.upatras.ece.nam.baker.model.BakerProperty;
import gr.upatras.ece.nam.baker.model.BakerUser;
import gr.upatras.ece.nam.baker.model.BunMetadata;
import gr.upatras.ece.nam.baker.model.Category;
import gr.upatras.ece.nam.baker.model.InstalledBun;
import gr.upatras.ece.nam.baker.model.Product;
import gr.upatras.ece.nam.baker.model.SubscribedMachine;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author ctranoris
 *
 */
public class BakerRepository {

	private static final transient Log logger = LogFactory.getLog(BakerRepository.class.getName());
	private static BakerJpaController bakerJpaController;
	
	
	
	public BakerRepository(){
	}
	
	
	/**
	 * Add new baker user
	 * 
	 * @param s
	 *            BakerUser to add
	 * @return the BakerUser
	 */
	public BakerUser addBakerUserToUsers(BakerUser s) {
		bakerJpaController.saveUser(s);
		return s;
	}
	
//	public BunMetadata addBunMetadataToBuns(BunMetadata bm){
//		bakerJpaController.saveBunMetadata(bm);
//		return bm;
//	}
//	
	public Collection<BakerUser> getUserValues() {

		List<BakerUser> ls = bakerJpaController.readUsers(0, 100000);
//		HashMap<Integer, BakerUser> cb = new HashMap<>();
//		
//		for (BakerUser buser : ls) {
//			cb.put(buser.getId() , buser);
//		}
		
		return ls;
	}
	
	public BakerUser updateUserInfo(int userid, BakerUser user) {
		BakerUser bm = bakerJpaController.updateBakerUser(user);
		return bm;
	}
	
//	public BunMetadata updateBunInfo(long l, BunMetadata bm) {
//		BunMetadata bmr = bakerJpaController.updateBunMetadata(bm);
//		return bmr;
//	}
	
	public Product updateProductInfo(Product bm) {
		Product bmr = bakerJpaController.updateProduct(bm);
		return bmr;
	}


	public void deleteUser(int userid) {
		bakerJpaController.deleteUser(userid);
	}

	public List<BunMetadata> getBuns(Long categoryid) {
		List<BunMetadata> ls = bakerJpaController.readBunsMetadata(categoryid,0, 100000);
		
		return ls;
	}
	
	public void deleteProduct(int bunid) {
		bakerJpaController.deleteProduct(bunid);
		
	}


	public BakerUser getUserByID(int userid) {
		return bakerJpaController.readBakerUserById(userid);
	}

	public BakerUser getUserByUsername(String un) {
		return bakerJpaController.readBakerUserByUsername(un);
	}
	

	public BakerUser getUserByEmail(String email) {
		return bakerJpaController.readBakerUserByEmail(email);
		}

	

	public Product getProductByID(long bunid) {
		return (Product) bakerJpaController.readProductByID(bunid);
	}
	
	public Product getProductByUUID(String uuid) {
		return (Product) bakerJpaController.readProductByUUID(uuid);
	}


	
	
	public BakerJpaController getBakerJpaController() {
		return bakerJpaController;
	}

	public void setBakerJpaController(BakerJpaController bakerJpaController) {
		this.bakerJpaController = bakerJpaController;
		logger.info("======================== SETing setBakerJpaController ========================");
		this.bakerJpaController.initData();
	}

	public Collection<SubscribedMachine> getSubscribedMachinesAsCollection() {

		List<SubscribedMachine> ls = bakerJpaController.readSubscribedMachines(0, 100000);
		
		return ls;
	}


	public SubscribedMachine getSubscribedMachineByID(int smId) {
		return bakerJpaController.readSubscribedMachineById(smId);
	}


	public SubscribedMachine addSubscribedMachine(SubscribedMachine sm) {
		bakerJpaController.saveSubscribedMachine(sm);
		return sm;
	}


	public SubscribedMachine updateSubscribedMachineInfo(int smId, SubscribedMachine sm) {
		SubscribedMachine bm = bakerJpaController.updateSubscribedMachine(sm);
		return bm;
	}


	public void deleteSubscribedMachine(int smId) {
		bakerJpaController.deleteSubscribedMachine(smId);
		
	}


	/**
	 * returns first 100000 apps only :-)
	 * @param categoryid 
	 * @return list of apps
	 */
	public List<ApplicationMetadata> getApps(Long categoryid) {
		List<ApplicationMetadata> ls = bakerJpaController.readAppsMetadata(categoryid, 0, 100000);		
		return ls;
	}


//	public ApplicationMetadata getApplicationMetadataByID(int appid) {
//		return (ApplicationMetadata) bakerJpaController.readProductByID(appid);
//	}
//
//
//	public ApplicationMetadata getApplicationMetadataByUUID(String uuid) {
//		return (ApplicationMetadata) bakerJpaController.readProductByUUID(uuid);
//	}


//	public ApplicationMetadata updateApplicationInfo(int appid, ApplicationMetadata sm) {
//		ApplicationMetadata bmr = bakerJpaController.updateApplicationMetadata(sm);
//		return bmr;
//		
//	}


	public Object getCategories() {

		List<Category> ls = bakerJpaController.readCategories(0, 100000);
		return ls;	}


	public Category addCategory(Category c) {
		bakerJpaController.saveCategory(c);
		return c;
	}


	public Category getCategoryByID(int catid) {
		return bakerJpaController.readCategoryByID(catid);
	}


	public Category updateCategoryInfo(Category c) {
		Category bmr = bakerJpaController.updateCategory(c);
		return bmr;
	}


	public void deleteCategory(int catid) {
		bakerJpaController.deleteCategory(catid);
		
	}


	
	public BakerProperty addproperty(BakerProperty p) {
		bakerJpaController.saveProperty(p);
		return p;
	}

	public void deleteProperty(int propid) {
		bakerJpaController.deleteProperty(propid);
		
	}
	

	public BakerProperty updateProperty(BakerProperty p) {
		BakerProperty bp = bakerJpaController.updateProperty(p);
		return bp;
	}

	public Object getProperties() {

		List<BakerProperty> ls = bakerJpaController.readProperties(0, 100000);
		return ls;	
	}
	
	public static BakerProperty getPropertyByName(String name) {
		return bakerJpaController.readPropertyByName(name);
	}


	public BakerProperty getPropertyByID(int propid) {
		return bakerJpaController.readPropertyByID(propid);
	}
}
