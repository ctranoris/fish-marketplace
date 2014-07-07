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

import gr.upatras.ece.nam.baker.model.InstalledService;

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
 * @author ctranoris
 *
 */
public class BakerJpaController {
	private static final transient Log logger = LogFactory.getLog(BakerJpaController.class.getName());

	//@PersistenceContext(unitName = "bakerdb").
	
	 @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
	
	
	//private EntityManager entityManager;
	

//	public EntityManager getEntityManager() {
//		return entityManager;
//	}
//
//	public void setEntityManager(EntityManager entityManager) {
//		this.entityManager = entityManager;
//	}
	
	

	public long countInstalledServices() {

		EntityManager entityManager = entityManagerFactory.createEntityManager();

        
		Query q = entityManager.createQuery("SELECT COUNT(s) FROM InstalledService s") ;
		return (Long) q.getSingleResult();
	}
	
	
	public InstalledService update(InstalledService is) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction entityTransaction = entityManager.getTransaction();

        entityTransaction.begin();
        InstalledService resis = entityManager.merge(is);
        entityTransaction.commit();
        
		return resis;
	}

	public void saveInstalledService(InstalledService is) {
    	logger.info("Will create InstalledService = " +is.getUuid());

    	EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction entityTransaction = entityManager.getTransaction();

        entityTransaction.begin();
        
		entityManager.persist(is);
		entityManager.flush();
        entityTransaction.commit();
	}

	public InstalledService readInstalledServiceByName(final String name) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query q = entityManager.createQuery("SELECT m FROM InstalledService m WHERE m.name='"+name+"'") ;
		return (InstalledService) q.getSingleResult();
	}
	
	public InstalledService readInstalledServiceByUUID(final String uuid) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query q = entityManager.createQuery("SELECT m FROM InstalledService m WHERE m.uuid='"+uuid +"'") ;
		return (InstalledService) q.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<InstalledService> read(int firstResult, int maxResults) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query q = entityManager.createQuery("SELECT m FROM InstalledService m") ;
		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);
		return q.getResultList();
	}
	
	
	

//	public InstalledService update(InstalledService is) {
//    	logger.info("Will update InstalledService = " +is.getUuid());
//    	logger.info("Will update entityManager = " + entityManager);
//		
//    	InstalledService resis = entityManager.merge(is);
//    			
//		return resis;
//	}

	public void delete(final InstalledService message) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction entityTransaction = entityManager.getTransaction();

        entityTransaction.begin();
        
		entityManager.remove(message);
		
		entityTransaction.commit();
	}
	
	
	 public void getAll() {
     	logger.info("================= getAll() ==================START");

		EntityManager entityManager = entityManagerFactory.createEntityManager();
     	
	        List<InstalledService>lb = entityManager.createQuery( "select p from InstalledService p").getResultList();
	        for (Iterator iterator = lb.iterator(); iterator.hasNext();) {
	        	InstalledService iservice = (InstalledService) iterator.next();
				logger.info("=== InstalledService found: "+iservice.getName() +
						" Uuid: "+iservice.getUuid()+
						" RepoUrl: "+iservice.getRepoUrl()+
						" InstalledVersion: "+iservice.getInstalledVersion()+
						" Status: "+iservice.getStatus() );
				if (iservice.getServiceMetadata()!=null)
					logger.info("=== InstalledServiceMetadata found: "+iservice.getServiceMetadata().getName() +
							" Uuid: "+iservice.getServiceMetadata().getUuid()+
							" getPackageLocation: "+iservice.getServiceMetadata().getPackageLocation()+
							" getVersion: "+iservice.getServiceMetadata().getVersion() );
				
			}
	     
	       logger.info("================= getAll() ==================END");
	    }
	 
	
	 
	public BakerJpaController() {
		logger.info(">>>>>>>>>>>>>> BrokerServiceImpl constructor  <<<<<<<<<<<<<<<<<<");
	}
	
    public String echo(String message) {
        return "Echo processed: " + message;
        
    }

		
	public void deleteAllInstalledService() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction entityTransaction = entityManager.getTransaction();

        entityTransaction.begin();
		
		Query q = entityManager.createQuery("DELETE FROM InstalledService ") ;
		q.executeUpdate();
		entityManager.flush();
		
		entityTransaction.commit();
	}
}
