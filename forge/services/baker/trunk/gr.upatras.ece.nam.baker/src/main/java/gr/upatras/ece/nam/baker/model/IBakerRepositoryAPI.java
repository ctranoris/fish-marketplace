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

package gr.upatras.ece.nam.baker.model;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;


public interface IBakerRepositoryAPI {

	//utility

	Response getEntityImage(String uuid, String imgfile);
	
	//USER related methods 
	Response getUsers(); 
	Response getUserById(int userid);
	Response addUser(BakerUser user);
	Response updateUserInfo(int userid, BakerUser user);
	Response deleteUser(int userid);
	Response getAllBunsofUser(int userid);
	Response getAllAppsofUser(int userid);

	//Sessions
	public Response addUserSession(UserSession userSession);
	public Response getUserSessions();
	
	//categories
	Response getCategories();
	Response getCategoryById(int catid);
	Response addCategory(Category c);
	Response updateCategory(int catId, Category c);
	Response deleteCategory(int catId);
	
	//BUNs related API methods
	Response getBuns();
	Response getBunMetadataByID(int bunid);
	Response getBunMetadataByUUID(String uuid);		
	Response getBunofUser( int userid, int bunid);
	Response downloadBunPackage(String uuid, String bunfile);	
	void updateBunMetadata(int bid, int userid, String bunname, int bunid, String uuid, String shortDescription, String longDescription, String version,
			Attachment image, Attachment bunFile);
	void addBunMetadata(int userid, String bunname, String shortDescription, String longDescription, String version, Attachment image, Attachment bunFile);
	void deleteBun( int bunid);
	
	//Subscribed machines
	Response getSubscribedMachines();
	Response getSubscribedMachineById(int smId);
	Response addSubscribedMachine(SubscribedMachine sm);
	Response updateSubscribedMachine(int smId, SubscribedMachine sm);
	Response deleteSubscribedMachine(int smId);
	
	//Apps Related API methods
	Response getApps();
	Response getAppMetadataByID(int appid);
	Response getAppMetadataByUUID(String uuid);		
	Response getAppofUser( int userid, int bunid);
	void updateAppMetadata(int aid, int userid, String appname, int appid, String uuid, String shortDescription, String longDescription, String version,
			Attachment image);
	Response addAppMetadata(int userid, String appname, String shortDescription, String longDescription, String version, Attachment image);
	void deleteApp(int appid);
	
}
