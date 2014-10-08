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

import gr.upatras.ece.nam.baker.model.ApplicationMetadata;
import gr.upatras.ece.nam.baker.model.BakerUser;
import gr.upatras.ece.nam.baker.model.BunMetadata;
import gr.upatras.ece.nam.baker.model.InstalledBun;
import gr.upatras.ece.nam.baker.model.SubscribedMachine;

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


	public void initData() {
		BakerUser admin = readBakerUserByUsername("admin");
		logger.info("======================== admin  = " + admin);
		
		if (admin==null){
			BakerUser bu = new BakerUser();
			bu.setName("Baker Administrator");
			bu.setUsername("admin");
			bu.setPassword("changeme");
			bu.setEmail("");
			bu.setOrganization("");
			bu.setRole("ROLE_BOSS");			
			saveUser(bu);
		}
		
	}

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
		return (q.getResultList().size()==0)?null:(InstalledBun) q.getSingleResult();
	}

	public InstalledBun readInstalledBunByUUID(final String uuid) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query q = entityManager.createQuery("SELECT m FROM InstalledBun m WHERE m.uuid='" + uuid + "'");
		return (q.getResultList().size()==0)?null:(InstalledBun) q.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<InstalledBun> readInstalledBuns(int firstResult, int maxResults) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query q = entityManager.createQuery("SELECT m FROM InstalledBun m");
		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);
		return q.getResultList();
	}

	public void deleteInstalledBun(final InstalledBun message) {
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
	
	public void deleteAllAppMetadata() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();

		Query q = entityManager.createQuery("DELETE FROM ApplicationMetadata ");
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
	
	@SuppressWarnings("unchecked")
	public List<BakerUser> readUsers(int firstResult, int maxResults) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query q = entityManager.createQuery("SELECT m FROM BakerUser m");
		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);
		return q.getResultList();
	}
	
	public long countUsers() {

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query q = entityManager.createQuery("SELECT COUNT(s) FROM BakerUser s");
		return (Long) q.getSingleResult();
	}

	public void getAllUsersPrinted() {
		logger.info("================= getAllUsers() ==================START");

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		List<BakerUser> lb = entityManager.createQuery("select p from BakerUser p").getResultList();
		for (Iterator iterator = lb.iterator(); iterator.hasNext();) {
			BakerUser bu = (BakerUser) iterator.next();
			logger.info("	======> BakerUser found: " + bu.getName() + ", Id: " + bu.getId() + ", Id: " + bu.getOrganization() + ", username: " + bu.getUsername());

			List<BunMetadata> buns = bu.getBuns();
			for (BunMetadata bunMetadata : buns) {
				logger.info("	======> bunMetadata found: " + bunMetadata.getName() + ", Id: " + bunMetadata.getId() + ", getUuid: " + bunMetadata.getUuid()
						+ ", getName: " + bunMetadata.getName());
			}
			List<ApplicationMetadata> apps = bu.getApps();
			for (ApplicationMetadata appnMetadata : apps) {
				logger.info("	======> appnMetadata found: " + appnMetadata.getName() + ", Id: " + appnMetadata.getId() 
						+ ", getUuid: " + appnMetadata.getUuid()
						+ ", getName: " + appnMetadata.getName());
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
		logger.info("================= updateBunMetadata ==================");
		logger.info("bmgetId="+bm.getId());
		logger.info("bm getName= "+bm.getName());
		logger.info("bm getPackageLocation= "+bm.getPackageLocation());
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();
		BunMetadata resis = entityManager.merge(bm);
		entityTransaction.commit();

		return resis;
	}
	
	@SuppressWarnings("unchecked")
	public List<BunMetadata> readBunsMetadata(int firstResult, int maxResults) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query q = entityManager.createQuery("SELECT m FROM BunMetadata m");
		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);
		return q.getResultList();
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
		return (q.getResultList().size()==0)?null:(BakerUser) q.getSingleResult();
	}
	
	public BakerUser readBakerUserById(int userid) {
		
		
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		return entityManager.find(BakerUser.class, userid);
		
//		Query q = entityManager.createQuery("SELECT m FROM BakerUser m WHERE m.id=" + userid );		
//		return (q.getResultList().size()==0)?null:(BakerUser) q.getSingleResult();
		
	}

	public BunMetadata readBunMetadataByUUID(String uuid) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query q = entityManager.createQuery("SELECT m FROM BunMetadata m WHERE m.uuid='" + uuid + "'");
		return (q.getResultList().size()==0)?null:(BunMetadata) q.getSingleResult();
	}
	
	public BunMetadata readBunMetadataByID(int bunid) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		BunMetadata u = entityManager.find(BunMetadata.class, bunid);
		return u;
	}


	public void deleteUser(int userid) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		BakerUser u = entityManager.find(BakerUser.class, userid);
		
		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();

		entityManager.remove(u);

		entityTransaction.commit();
	}

	public void deleteBun(int bunId) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		BunMetadata bun = entityManager.find(BunMetadata.class, bunId);
		

		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();

		entityManager.remove(bun);

		entityTransaction.commit();
	}

	
	public void saveSubscribedMachine(SubscribedMachine sm) {
		logger.info("Will save SubscribedMachine = " + sm.getURL());

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();

		entityManager.persist(sm);
		entityManager.flush();
		entityTransaction.commit();

	}
	
	public SubscribedMachine updateSubscribedMachine(SubscribedMachine sm) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();
		SubscribedMachine resis = entityManager.merge(sm);
		entityTransaction.commit();

		return resis;
	}

	public SubscribedMachine readSubscribedMachineById(int userid) {
		
		
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		return entityManager.find(SubscribedMachine.class, userid);
		
	}
	
	public void deleteSubscribedMachine(int smId) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		SubscribedMachine sm = entityManager.find(SubscribedMachine.class, smId);	

		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		entityManager.remove(sm);
		entityTransaction.commit();
	}
	
	public long countSubscribedMachines() {

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query q = entityManager.createQuery("SELECT COUNT(s) FROM SubscribedMachine s");
		return (Long) q.getSingleResult();
	}

	
	public void getAllSubscribedMachinesPrinted() {
		logger.info("================= getSubscribedMachine() ==================START");

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		List<SubscribedMachine> lb = entityManager.createQuery("select p from SubscribedMachine p").getResultList();
		for (Iterator iterator = lb.iterator(); iterator.hasNext();) {
			SubscribedMachine sm = (SubscribedMachine) iterator.next();
			logger.info("	======> SubscribedMachine found: " + sm.getURL() + ", Id: " + sm.getId()  );			

		}
	}

	public void deleteAllSubscribedMachines() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();

		Query q = entityManager.createQuery("DELETE FROM SubscribedMachine ");
		q.executeUpdate();
		entityManager.flush();

		entityTransaction.commit();
		
	}

	public List<SubscribedMachine> readSubscribedMachines(int firstResult, int maxResults) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query q = entityManager.createQuery("SELECT m FROM SubscribedMachine m");
		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);
		return q.getResultList();
	}

	public ApplicationMetadata updateApplicationMetadata(ApplicationMetadata appmeta) {
		logger.info("================= updateApplicationMetadata ==================");
		logger.info("appmetagetId="+appmeta.getId());
		logger.info("appmeta getName= "+appmeta.getName());
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();
		ApplicationMetadata resis = entityManager.merge(appmeta);
		entityTransaction.commit();

		return resis;
		
	}


	public ApplicationMetadata readApplicationMetadataByUUID(String uuid) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query q = entityManager.createQuery("SELECT m FROM ApplicationMetadata m WHERE m.uuid='" + uuid + "'");
		return (q.getResultList().size()==0)?null:(ApplicationMetadata) q.getSingleResult();
	}

	public List<ApplicationMetadata> readAppsMetadata(int firstResult, int maxResults) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query q = entityManager.createQuery("SELECT m FROM ApplicationMetadata m");
		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);
		return q.getResultList();
	}

	public ApplicationMetadata readApplicationMetadataByID(int appid) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		ApplicationMetadata u = entityManager.find(ApplicationMetadata.class, appid);
		return u;
	}

	public void deleteApp(int appid) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		ApplicationMetadata bun = entityManager.find(ApplicationMetadata.class, appid);

		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();

		entityManager.remove(bun);

		entityTransaction.commit();
		
	}

}
