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
import gr.upatras.ece.nam.baker.model.Category;
import gr.upatras.ece.nam.baker.model.Course;
import gr.upatras.ece.nam.baker.model.FIREAdapter;
import gr.upatras.ece.nam.baker.model.InstalledBun;
import gr.upatras.ece.nam.baker.model.Product;
import gr.upatras.ece.nam.baker.model.SubscribedMachine;
import gr.upatras.ece.nam.baker.model.Widget;

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
			
			Category c = new Category();
			c.setName("None");
			saveCategory(c);
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
	
	public void deleteAllProducts() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();

		Query q = entityManager.createQuery("DELETE FROM Product ");
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
//		List<ApplicationMetadata> apps = bu.getApps();
//		for (ApplicationMetadata app : apps) {			
//			entityManager.persist(app.getCategory());
//			entityManager.persist(app);
//		}
//		
//		List<BunMetadata> buns = bu.getBuns() ;
//		for (BunMetadata bun : buns) {			
//			entityManager.persist(bun.getCategory());
//			entityManager.persist(bun);
//		}
		
		
		entityManager.flush();
		entityTransaction.commit();

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

			List<Product> products = bu.getProducts();
			for (Product prod : products) {
				logger.info("	======> bunMetadata found: " + prod.getName() + 
						", Id: " + prod.getId() + ", getUuid: " + prod.getUuid()
						+ ", getName: " + prod.getName());
			}
			

		}
		logger.info("================= getAll() ==================END");
	}

	public void saveProduct(Product prod) {
		logger.info("Will save Product = " + prod.getName());

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		entityManager.persist(prod);
		entityManager.flush();
		entityTransaction.commit();

	}
	
	public Product updateProduct(Product bm) {
		logger.info("================= updateProduct ==================");
		logger.info("bmgetId="+bm.getId());
		logger.info("bm getName= "+bm.getName());
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();
		Product resis = entityManager.merge(bm);
		entityTransaction.commit();

		return resis;
	}

	
//	public BunMetadata updateBunMetadata(BunMetadata bm) {
//		logger.info("================= updateBunMetadata ==================");
//		logger.info("bmgetId="+bm.getId());
//		logger.info("bm getName= "+bm.getName());
//		logger.info("bm getPackageLocation= "+bm.getPackageLocation());
//		EntityManager entityManager = entityManagerFactory.createEntityManager();
//
//		EntityTransaction entityTransaction = entityManager.getTransaction();
//
//		entityTransaction.begin();
//		BunMetadata resis = entityManager.merge(bm);
//		entityTransaction.commit();
//
//		return resis;
//	}
	
	@SuppressWarnings("unchecked")
	public List<BunMetadata> readBunsMetadata(Long categoryid, int firstResult, int maxResults) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		//Query q = entityManager.createQuery("SELECT m FROM BunMetadata m");
		Query q;
		
		if ((categoryid!=null) && (categoryid>=0))
			q = entityManager.createQuery("SELECT a FROM BunMetadata a WHERE a.categories.id="+categoryid+" ORDER BY a.id");
		else
			q = entityManager.createQuery("SELECT a FROM BunMetadata a ORDER BY a.id");

		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);
		return q.getResultList();
	}
	
	public List<FIREAdapter> readFIREAdaptersMetadata(Long categoryid, int firstResult, int maxResults) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Query q;
		
		if ((categoryid!=null) && (categoryid>=0))
			q = entityManager.createQuery("SELECT a FROM FIREAdapter a WHERE a.categories.id="+categoryid+" ORDER BY a.id");
		else
			q = entityManager.createQuery("SELECT a FROM FIREAdapter a ORDER BY a.id");

		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);
		return q.getResultList();
	}


	public Product readProductByUUID(String uuid) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query q = entityManager.createQuery("SELECT m FROM Product m WHERE m.uuid='" + uuid + "'");
		return (q.getResultList().size()==0)?null:(Product) q.getSingleResult();
	}
	
	public Product readProductByID(int id) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Product u = entityManager.find(Product.class, id);
		return u;
	}

//	public BunMetadata readBunMetadataByUUID(String uuid) {
//		EntityManager entityManager = entityManagerFactory.createEntityManager();
//
//		Query q = entityManager.createQuery("SELECT m FROM BunMetadata m WHERE m.uuid='" + uuid + "'");
//		return (q.getResultList().size()==0)?null:(BunMetadata) q.getSingleResult();
//	}
//	
//	public BunMetadata readBunMetadataByID(int bunid) {
//		EntityManager entityManager = entityManagerFactory.createEntityManager();
//		BunMetadata u = entityManager.find(BunMetadata.class, bunid);
//		return u;
//	}


	public void deleteUser(int userid) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		BakerUser u = entityManager.find(BakerUser.class, userid);
		
		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();

		entityManager.remove(u);

		entityTransaction.commit();
	}
	
	public void deleteProduct(int id) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Product p = entityManager.find(Product.class, id);		

		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();
		entityManager.remove(p);
		entityTransaction.commit();
	}

	@SuppressWarnings("unchecked")
	public List<Product> readProducts(Long categoryid, int firstResult, int maxResults) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Query q;
		
		if ((categoryid!=null) && (categoryid>=0))
			q = entityManager.createQuery("SELECT a FROM Product a WHERE a.category.id="+categoryid+" ORDER BY a.id");
		else
			q = entityManager.createQuery("SELECT a FROM Product a ORDER BY a.id");

		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);
		return q.getResultList();
	}

	
	public void getAllProductsPrinted() {
		logger.info("================= getAllProductsPrinted() ==================START");

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		List<Product> lb = readProducts(null,0,10000);
		for (Iterator iterator = lb.iterator(); iterator.hasNext();) {
			Product prod = (Product) iterator.next();
			
				logger.info("	=================> Product found: " + prod.getName() + ", Id: " + prod.getId() + ", getUuid: " + prod.getUuid()
						+ ", getName: " + prod.getName()
						+ ", Owner.name: " + prod.getOwner().getName() );
			

		}
		logger.info("================= getAllProductsPrinted() ==================END");

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



	public List<ApplicationMetadata> readAppsMetadata(Long categoryid, int firstResult, int maxResults) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Query q;
		
		if ((categoryid!=null) && (categoryid>=0))
			q = entityManager.createQuery("SELECT a FROM ApplicationMetadata a WHERE a.categories.id="+categoryid+" ORDER BY a.id");
		else
			q = entityManager.createQuery("SELECT a FROM ApplicationMetadata a ORDER BY a.id");
		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);
		return q.getResultList();
	}

	public List<Category> readCategories(int firstResult, int maxResults) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query q = entityManager.createQuery("SELECT m FROM Category m  ORDER BY m.id");
		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);
		return q.getResultList();
	}

	public void saveCategory(Category c) {
		logger.info("Will category = " + c.getName());

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		entityManager.persist(c);
		entityManager.flush();
		entityTransaction.commit();
	}

	public Category readCategoryByID(int catid) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Category u = entityManager.find(Category.class, catid);
		return u;
	}

	public Category updateCategory(Category c) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();
		Category  resis = entityManager.merge(c);
		entityTransaction.commit();

		return resis;
	}

	public void deleteCategory(int catid) {

		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Category c = entityManager.find(Category.class, catid);

		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		entityManager.remove(c);
		entityTransaction.commit();
		
	}

	public void deleteAllCategories() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();

		Query q = entityManager.createQuery("DELETE FROM Category");
		q.executeUpdate();
		entityManager.flush();

		entityTransaction.commit();
		
	}

	public void getAllCategoriesPrinted() {
		logger.info("================= getAllCategoriesPrinted() ==================START");

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		List<Category> lb = entityManager.createQuery("select p from Category p").getResultList();
		for (Iterator iterator = lb.iterator(); iterator.hasNext();) {
			Category sm = (Category) iterator.next();
			logger.info("	======> Category found: " + sm.getName() + ", Id: " + sm.getId()  );			

		}
		
	}


	
	public List<Widget> readWidgetMetadata(Long categoryid, int firstResult, int maxResults) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Query q;
		
		if ((categoryid!=null) && (categoryid>=0))
			q = entityManager.createQuery("SELECT a FROM Widget a WHERE a.categories.id="+categoryid+" ORDER BY a.id");
		else
			q = entityManager.createQuery("SELECT a FROM Widget a ORDER BY a.id");
		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);
		return q.getResultList();
	}

	public List<Course> readCoursesMetadata(Long categoryid, int firstResult, int maxResults) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Query q;
		
		if ((categoryid!=null) && (categoryid>=0))
			q = entityManager.createQuery("SELECT a FROM Course a WHERE a.categories.id="+categoryid+" ORDER BY a.id");
		else
			q = entityManager.createQuery("SELECT a FROM Course a ORDER BY a.id");
		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);
		return q.getResultList();
	}

	

	
}
