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

package gr.upatras.ece.nam.baker.impl;

import gr.upatras.ece.nam.baker.model.IRepositoryWebClient;
import gr.upatras.ece.nam.baker.model.InstalledService;
import gr.upatras.ece.nam.baker.model.InstalledServiceStatus;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BakerInstallationMgmt {

	private ConcurrentHashMap<String, InstalledService> managedServices;
	private IRepositoryWebClient repoWebClient;
	private BakerJpaController bakerJpaController;

	private static final transient Log logger = LogFactory.getLog(BakerInstallationMgmt.class.getName());

	public BakerInstallationMgmt() {
		managedServices = new ConcurrentHashMap<>();
		// this.setRepoWebClient(new RepositoryWebClient());
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
		// InstalledService installService = bakerJpaController.readInstalledServiceByUUID(s.getUuid());
		// logger.info("Saved task for uuid :" + installService.getUuid() + " is:" + installService.getStatus());

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
	public InstalledService installServiceAndStart(String uuid, String repourl) {

		logger.info("installServiceAndStart " + uuid);
		InstalledService s = managedServices.get(uuid); // return existing if
														// found
		if ((s != null) && (s.getStatus() != InstalledServiceStatus.FAILED) && (s.getStatus() != InstalledServiceStatus.UNINSTALLED)) {
			return s;
		}

		logger.info("will start installation");

		if (s == null) {
			s = new InstalledService(uuid, repourl);
			addServiceToManagedServices(s);
			bakerJpaController.saveInstalledService(s);
		} else if ((s.getStatus() == InstalledServiceStatus.FAILED) || (s.getStatus() == InstalledServiceStatus.UNINSTALLED)) {

			logger.info("Will RESTART installation of existing" + s.getUuid() + ". HAD Status= " + s.getStatus());
			s.setStatus(InstalledServiceStatus.INIT); // restart installation
			s.setRepoUrl(repourl);
		}

		processServiceLifecylceJob(s, this.bakerJpaController, InstalledServiceStatus.STARTED);
		return s;
	}

	/**
	 * It executes the installation of the bun in a thread job, following the bun installation state machine
	 * 
	 * @param s
	 *            InstalledService object to manage the lifecycle
	 */
	private void processServiceLifecylceJob(final InstalledService s, final BakerJpaController jpsctr, final InstalledServiceStatus targetStatus) {

		logger.info("Creating new thread of " + s.getUuid() + " for target action = " + targetStatus);
		Thread t1 = new Thread(new Runnable() {
			public void run() {

				new ServiceLifecycleMgmt(s, repoWebClient, jpsctr, targetStatus);

			}
		});
		t1.start();

		// Runnable run = new InstallationTask(s, repoWebClient);
		// Thread thread = new Thread(run);
		// thread.start();

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

	public BakerJpaController getBakerJpaController() {
		return bakerJpaController;
	}

	public void setBakerJpaController(BakerJpaController b) {
		this.bakerJpaController = b;

		this.bakerJpaController = b;
		List<InstalledService> ls = b.read(0, 100000);

		for (InstalledService installedService : ls) {
			managedServices.put(installedService.getUuid(), installedService);
		}
	}

	public void stopService(String uuid) {
		InstalledService is = managedServices.get(uuid);

		if (is.getStatus() != InstalledServiceStatus.STARTED)
			return;

		logger.info("will stop service uuid= " + uuid);

		// is.setStatus(InstalledServiceStatus.STOPPING); //

		processServiceLifecylceJob(is, this.bakerJpaController, InstalledServiceStatus.STOPPED);

	}

	public void uninstallService(String uuid) {
		InstalledService is = managedServices.get(uuid);

		logger.info("will uninstall service uuid= " + uuid);

		// is.setStatus(InstalledServiceStatus.UNINSTALLING); //

		processServiceLifecylceJob(is, this.bakerJpaController, InstalledServiceStatus.UNINSTALLED);

	}

	public void configureService(String uuid) {
		InstalledService is = managedServices.get(uuid);

		logger.info("will configure service uuid= " + uuid);

		processServiceLifecylceJob(is, this.bakerJpaController, InstalledServiceStatus.STARTED);

	}

}
