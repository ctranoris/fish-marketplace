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
import gr.upatras.ece.nam.baker.model.BakerUser;
import gr.upatras.ece.nam.baker.model.BunMetadata;
import gr.upatras.ece.nam.baker.model.InstalledBun;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BakerRepository {

	private static final transient Log logger = LogFactory.getLog(BakerRepository.class.getName());
	private BakerJpaController bakerJpaController;
	
	
	
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
	
	public BunMetadata addBunMetadataToBuns(BunMetadata bm){
		bakerJpaController.saveBunMetadata(bm);
		return bm;
	}
	
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
	
	public BunMetadata updateBunInfo(int bunid, BunMetadata bm) {
		BunMetadata bmr = bakerJpaController.updateBunMetadata(bm);
		return bmr;
	}


	public void deleteUser(int userid) {
		bakerJpaController.deleteUser(userid);
	}

	public List<BunMetadata> getBuns() {
		List<BunMetadata> ls = bakerJpaController.readBunsMetadata(0, 100000);
		
		return ls;
	}
	
	public void deleteBun(int bunid) {
		bakerJpaController.deleteBun(bunid);
		
	}


	public BakerUser getUserByID(int userid) {
		return bakerJpaController.readBakerUserById(userid);
	}
	
	

	public BunMetadata getBunByID(int bunid) {
		return bakerJpaController.readBunMetadataByID(bunid);
	}
	
	public BunMetadata getBunByUUID(String uuid) {
		return bakerJpaController.readBunMetadataByUUID(uuid);
	}


	
	
	public BakerJpaController getBakerJpaController() {
		return bakerJpaController;
	}

	public void setBakerJpaController(BakerJpaController bakerJpaController) {
		this.bakerJpaController = bakerJpaController;
	}


	
	
	
}
