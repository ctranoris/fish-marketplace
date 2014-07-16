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

import gr.upatras.ece.nam.baker.model.BakerUser;
import gr.upatras.ece.nam.baker.model.BunMetadata;
import gr.upatras.ece.nam.baker.model.InstalledBun;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class maintains the entity manager and get a broker element from DB
 * 
 * @author ctranoris
 * 
 */
public class BakerJpaController {
	private static final transient Log logger = LogFactory.getLog(BakerJpaController.class.getName());

	// @PersistenceContext(unitName = "bakerdb").

	@PersistenceUnit
	private EntityManagerFactory entityManagerFactory;

	public long countInstalledBuns() {

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query q = entityManager.createQuery("SELECT COUNT(s) FROM InstalledBun s");
		return (Long) q.getSingleResult();
	}

	public InstalledBun updateInstalledBun(InstalledBun is) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();
		InstalledBun resis = entityManager.merge(is);
		entityTransaction.commit();

		return resis;
	}

	public void saveInstalledBun(InstalledBun is) {
		logger.info("Will create InstalledBun = " + is.getUuid());

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();

		entityManager.persist(is);
		entityManager.flush();
		entityTransaction.commit();
	}

	public InstalledBun readInstalledBunByName(final String name) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query q = entityManager.createQuery("SELECT m FROM InstalledBun m WHERE m.name='" + name + "'");
		return (InstalledBun) q.getSingleResult();
	}

	public InstalledBun readInstalledBunByUUID(final String uuid) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query q = entityManager.createQuery("SELECT m FROM InstalledBun m WHERE m.uuid='" + uuid + "'");
		return (InstalledBun) q.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<InstalledBun> read(int firstResult, int maxResults) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query q = entityManager.createQuery("SELECT m FROM InstalledBun m");
		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);
		return q.getResultList();
	}

	public void delete(final InstalledBun message) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();

		entityManager.remove(message);

		entityTransaction.commit();
	}

	public void getAllInstalledBunPrinted() {
		logger.info("================= getAll() ==================START");

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		List<InstalledBun> lb = entityManager.createQuery("select p from InstalledBun p").getResultList();
		for (Iterator iterator = lb.iterator(); iterator.hasNext();) {
			InstalledBun iBun = (InstalledBun) iterator.next();
			logger.info("=== InstalledBun found: " + iBun.getName() + ", Id: " + iBun.getId() + ", Uuid: " + iBun.getUuid() + ", RepoUrl: " + iBun.getRepoUrl()
					+ ", InstalledVersion: " + iBun.getInstalledVersion() + ", PackageURL: " + iBun.getPackageURL() + ", PackageLocalPath: "
					+ iBun.getPackageLocalPath() + ", Status: " + iBun.getStatus());

		}

		logger.info("================= getAll() ==================END");
	}

	public BakerJpaController() {
		logger.info(">>>>>>>>>>>>>> BakerJpaController constructor  <<<<<<<<<<<<<<<<<<");
	}

	public String echo(String message) {
		return "Echo processed: " + message;

	}

	public void deleteAllInstalledBuns() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();

		Query q = entityManager.createQuery("DELETE FROM InstalledBun ");
		q.executeUpdate();
		entityManager.flush();

		entityTransaction.commit();
	}

	public void deleteAllBunMetadata() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();

		Query q = entityManager.createQuery("DELETE FROM BunMetadata ");
		q.executeUpdate();
		entityManager.flush();

		entityTransaction.commit();

	}

	public void deleteAllUsers() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();

		Query q = entityManager.createQuery("DELETE FROM BakerUser ");
		q.executeUpdate();
		entityManager.flush();

		entityTransaction.commit();

	}

	public void saveUser(BakerUser bu) {
		logger.info("Will save BakerUser = " + bu.getName());

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();

		entityManager.persist(bu);
		entityManager.flush();
		entityTransaction.commit();

	}
	
	public BakerUser updateBakerUser(BakerUser bu) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();
		BakerUser resis = entityManager.merge(bu);
		entityTransaction.commit();

		return resis;
	}

	public void getAllUsersPrinted() {
		logger.info("================= getAllUsers() ==================START");

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		List<BakerUser> lb = entityManager.createQuery("select p from BakerUser p").getResultList();
		for (Iterator iterator = lb.iterator(); iterator.hasNext();) {
			BakerUser bu = (BakerUser) iterator.next();
			logger.info("	======> BakerUser found: " + bu.getName() + ", Id: " + bu.getId() + ", Id: " + bu.getOrganization() + ", Id: " + bu.getUsername());

			List<BunMetadata> buns = bu.getBuns();
			for (BunMetadata bunMetadata : buns) {

				logger.info("	======> bunMetadata found: " + bunMetadata.getName() + ", Id: " + bunMetadata.getId() + ", getUuid: " + bunMetadata.getUuid()
						+ ", getName: " + bunMetadata.getName());
			}

		}
		logger.info("================= getAll() ==================END");
	}

	public void saveBunMetadata(BunMetadata bmeta) {
		logger.info("Will save BunMetadata = " + bmeta.getName());

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		entityManager.persist(bmeta);
		entityManager.flush();
		entityTransaction.commit();

	}
	
	public BunMetadata updateBunMetadata(BunMetadata bm) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();
		BunMetadata resis = entityManager.merge(bm);
		entityTransaction.commit();

		return resis;
	}

	public void getAllBunsPrinted() {
		logger.info("================= getAllBunsPrinted() ==================START");

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		List<BunMetadata> lb = entityManager.createQuery("select p from BunMetadata p").getResultList();
		for (Iterator iterator = lb.iterator(); iterator.hasNext();) {
			BunMetadata bunMetadata = (BunMetadata) iterator.next();
			
				logger.info("	======> bunMetadata found: " + bunMetadata.getName() + ", Id: " + bunMetadata.getId() + ", getUuid: " + bunMetadata.getUuid()
						+ ", getName: " + bunMetadata.getName()
						+ ", Owner.name: " + bunMetadata.getOwner().getName() );
			

		}
		logger.info("================= getAllBunsPrinted() ==================END");

	}

	public BakerUser readBakerUserByUsername(String username) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Query q = entityManager.createQuery("SELECT m FROM BakerUser m WHERE m.username='" + username + "'");
		return (BakerUser) q.getSingleResult();
	}

	public BunMetadata readBunMetadataByUUID(String uuid) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query q = entityManager.createQuery("SELECT m FROM BunMetadata m WHERE m.uuid='" + uuid + "'");
		return (BunMetadata) q.getSingleResult();
	}
}
