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

package gr.upatras.ece.nam.baker.fiware;

import gr.upatras.ece.nam.baker.fiware.cloud.osconnector.JaxRs20Connector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.woorea.openstack.keystone.Keystone;
import com.woorea.openstack.keystone.api.TokensResource.Authenticate;
import com.woorea.openstack.keystone.model.Access;
import com.woorea.openstack.keystone.model.Access.Service;
import com.woorea.openstack.keystone.model.Access.Service.Endpoint;
import com.woorea.openstack.keystone.model.Tenant;
import com.woorea.openstack.keystone.model.Tenants;
import com.woorea.openstack.keystone.utils.KeystoneUtils;
import com.woorea.openstack.nova.Nova;
import com.woorea.openstack.nova.model.Server;
import com.woorea.openstack.nova.model.Servers;

public class FIWARECloudAccess {

	private static final transient Log logger = LogFactory.getLog(FIWARECloudAccess.class.getName());
	
	private static String KEYSTONE_AUTHURL = "http://cloud.lab.fi-ware.org:4731/v2.0";
	
	
	public static JaxRs20Connector getConnector(){
		return new JaxRs20Connector();
	}
	
	/**
	 * @param xAuthToken as given from OAUTH2 authentication session
	 * @return
	 */
	public static Keystone getKeystoneClient(String xAuthToken){
		JaxRs20Connector connector = new JaxRs20Connector();
		Keystone keysclient = new Keystone( KEYSTONE_AUTHURL , connector);
		keysclient.token(xAuthToken);
		return keysclient;
	}
	
	public static Tenant getFirstTenant(String xAuthToken){
		Keystone keysclient = getKeystoneClient(xAuthToken);
		Tenants tenants = keysclient.tenants().list().execute();
		logger.info("keystone.tenants().list()= " + tenants.getList().size());
		logger.info(keysclient.tenants().list().execute());
		String tenantName = tenants.getList().get(0).getId();
		logger.info("keystone tenantName= " + tenantName);
		if (tenants.getList().size()>0)
			return tenants.getList().get(0);
		else
			return null;
	}
	
	/**
	 * @param xAuthToken as given from OAUTH2 authentication session
	 * @return an Access model used for other actions
	 */
	public static Access getAccessModel(String xAuthToken){

		Tenant t = getFirstTenant(xAuthToken);
		return getAccessModel(t, xAuthToken);
	}
	
	/**
	 * @param xAuthToken as given from OAUTH2 authentication session
	 * @return an Access model used for other actions
	 */
	public static Access getAccessModel(Tenant t, String xAuthToken){
		

		Keystone keysclient = getKeystoneClient(xAuthToken);		
		Access a = keysclient.tokens().authenticate().withToken(xAuthToken).withTenantName(t.getId()).execute();
		return a;
	}
	
	/**
	 * @param xAuthToken as given from OAUTH2 authentication session
	 * @return
	 */
	public static String getFIWARENOVACloudAccessToken(String xAuthToken){
		
		Access a = getAccessModel(xAuthToken);		
		return a.getToken().getId();
		
	}
	
	/**
	 * @param xAuthToken as given from OAUTH2 authentication session
	 * @return
	 */
	public static List<Service> getServiceCatalog(String xAuthToken){
		Access a = getAccessModel(xAuthToken);		
		List<Service> scatalog = a.getServiceCatalog();	
		return scatalog;
	}
	
	
	/**
	 * @param xAuthToken as given from OAUTH2 authentication session
	 * @return
	 */
	public static List<Endpoint> getServiceCatalogEndpointsOnlyCompute(String xAuthToken){
		List<Service> scatalog = getServiceCatalog(xAuthToken);		
		ArrayList<Endpoint> result = new ArrayList<Endpoint>();
		for (Service service : scatalog) {
			List<Endpoint> endpoints = service.getEndpoints();
			for (Endpoint endpoint : endpoints) {
				if ( service.getType().equals("compute") )  
					result.add(endpoint);
			}
		}
		
		return result;
	}
	
	/**
	 * @param endPointPublicURL compute service public URL for selected region
	 * @param cloudAccessToken token as given from the keystone services
	 * @return
	 */
	public static ArrayList<Server> getServers(String endPointPublicURL, String cloudAccessToken){
		Nova novaClient = new Nova(endPointPublicURL , getConnector());
		novaClient.token( cloudAccessToken );
		// novaClient.enableLogging(Logger.getLogger("nova"), 100 * 1024);
		Servers servers = novaClient.servers().list(true).execute();
		ArrayList<Server> result = new ArrayList<Server>(); 		
		for (Server server : servers) {
			result.add(server);
		}
		
		
		return result;
	}
	

	public void showKeystone(String token) {

		JaxRs20Connector connector = new JaxRs20Connector();
		Keystone keysclient = new Keystone( KEYSTONE_AUTHURL , connector);
		// Keystone keysclient = new Keystone( "https://cloud.lab.fiware.org/keystone/v2.0/");

		keysclient.token(token);

		logger.info("keystone= " + keysclient);

		logger.info("keysclient.endpoints().list().toString()= " + keysclient.endpoints().list().toString());
		logger.info("keysclient.services().list().toString()= " + keysclient.services().list().toString());
		logger.info("keysclient.services().list().toString()= " + keysclient.services().list());

		// logger.info("access= "+access);
		logger.info("keystone.tenants()= " + keysclient.tenants());
		Tenants tenants = keysclient.tenants().list().execute();
		logger.info("keystone.tenants().list()= " + tenants.getList().size());
		logger.info(keysclient.tenants().list().execute());
		String tenantName = tenants.getList().get(0).getId();
		logger.info("keystone tenantName= " + tenantName);

		Access a = keysclient.tokens().authenticate().withToken(token).withTenantName(tenantName).execute();
		// logger.info("keystone  Access= "+ a.toString() );
		
		List<Service> scatalog = a.getServiceCatalog();
		
		//KeystoneUtils.findEndpointURL(scatalog, "nova", "Spain", "public");
		
		String spainPublicURL = "";
		for (Service service : scatalog) {
			List<Endpoint> endpoints = service.getEndpoints();
			String ep = "";
			for (Endpoint endpoint : endpoints) {
				ep += " Region: " + endpoint.getRegion() + ", publicURL" + endpoint.getPublicURL() + "\r\n";
				if ((service.getName().equals("nova")) && (endpoint.getRegion().equals("Spain"))) {
					spainPublicURL = endpoint.getPublicURL();
				}
			}
			logger.info("Service Name = " + service.getName() + "," + ep);
		}

		logger.info("keystone  user Access for Nova= " + a.getUser());
		logger.info("keystone  Token Access for Nova= " + a.getToken().getId());

		logger.info("=========== Nova========== ");

		// NovaClient novaClient = new NovaClient(KeystoneUtils.findEndpointURL(access.getServiceCatalog(), "compute", null, "public"),
		// access.getToken().getId());
		Nova novaClient = new Nova(spainPublicURL , connector);
		novaClient.token(a.getToken().getId());
		// novaClient.enableLogging(Logger.getLogger("nova"), 100 * 1024);
		Servers servers = novaClient.servers().list(true).execute();
		for (Server server : servers) {
			logger.info("===== Server ===== "+"\r\n"+ 
					"name = " + server.getName() +"\r\n"+
					"IPv4: "+server.getAccessIPv4()  +"\r\n"+
					"IPv6: "+server.getAccessIPv6()  +"\r\n"+
					"getAddresses: "+server.getAddresses().toString()  +"\r\n"+
					"getAvailabilityZone: "+server.getAvailabilityZone()  +"\r\n"+
					"getInstanceName: "+server.getInstanceName()  +"\r\n"+
					"Status: "+server.getStatus()  +"\r\n"+
					"getFlavorname: "+server.getFlavor().getName()  +"\r\n"
					
					);
		}

	}
}
