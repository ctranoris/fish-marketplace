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

import gr.upatras.ece.nam.baker.ServiceLifecycleMgmt;
import gr.upatras.ece.nam.baker.impl.BakerJpaController;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BakerService {

	private ConcurrentHashMap<String, InstalledService> managedServices;
	private IRepositoryWebClient repoWebClient;
	private BakerJpaController bakerJpaController;

	private static final transient Log logger = LogFactory.getLog(BakerService.class.getName());

	public BakerService() {
		managedServices = new ConcurrentHashMap<>();
		//this.setRepoWebClient(new RepositoryWebClient());
	}

	public ConcurrentHashMap<String, InstalledService> getManagedServices() {
		return managedServices;
	}

	/**
	 * Add installed service object to ManagedServices list
	 * 
	 * @param s
	 *            InstalledService to add
	 * @return the same service
	 */
	private InstalledService addServiceToManagedServices(InstalledService s) {
		managedServices.put(s.getUuid(), s);
		bakerJpaController.addInstalledService(s);
		
		return s;
	}

	private Boolean removeServiceFromManagedServices(InstalledService s) {
		InstalledService is = managedServices.remove(s.getUuid());
		bakerJpaController.delete(s);
		return (is != null);
	}

	/**
	 * Starts the installation of a service. If found already in local registry (managedservices list) returns a ref to an existing instance if found. Otherwise
	 * starts a new installation.
	 * 
	 * @param uuid
	 *            The uuid of the requested Bun
	 * @param repourl
	 *            The endpoint of the repository
	 * @return an InstalledService object
	 */
	public InstalledService installService(String uuid, String repourl) {

		InstalledService s = managedServices.get(uuid); // return existing if
														// found
		if (( s!=null ) && (s.getStatus() != InstalledServiceStatus.FAILED) ){
			return s;
		}
		

		logger.info("will start installation");

		if (s == null) {
			s = new InstalledService(uuid, repourl);
			addServiceToManagedServices(s);
		} else if (s.getStatus() == InstalledServiceStatus.FAILED) {
			s.setStatus(InstalledServiceStatus.INIT); //restart installation
			s.setRepoUrl(repourl);
		}

		processServiceLifecylceJob(s);
		return s;
	}

	/**
	 * It executes the installation of the bun in a thread job, following the bun installation state machine
	 * 
	 * @param s InstalledService object to manage the lifecycle
	 */
	private void processServiceLifecylceJob(final InstalledService s) {

		logger.info("Creating new thread");
		Thread t1 = new Thread(new Runnable() {
			public void run() {

				new ServiceLifecycleMgmt(s, repoWebClient, bakerJpaController);

			}
		});
		t1.start();

		// Runnable run = new InstallationTask(s, repoWebClient);
		// Thread thread = new Thread(run);
		// thread.start();

	}

	public Boolean uninstallService(String uuid) {
		InstalledService is = getService(uuid);
		Boolean res = removeServiceFromManagedServices(is);
		return res;
	}

	public InstalledService getService(String uuid) {
		InstalledService is = managedServices.get(uuid);
		return is;
	}

	public IRepositoryWebClient getRepoWebClient() {
		return repoWebClient;
	}

	public void setRepoWebClient(IRepositoryWebClient repoWebClient) {
		this.repoWebClient = repoWebClient;
	}

	public void setJpaController(BakerJpaController jpactrl) {
		this.bakerJpaController = jpactrl;
		
	}

}
