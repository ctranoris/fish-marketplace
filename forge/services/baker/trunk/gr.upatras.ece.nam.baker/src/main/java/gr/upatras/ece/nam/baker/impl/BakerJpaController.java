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
 * @author ctranoris
 *
 */
public class BakerJpaController {
	private static final transient Log logger = LogFactory.getLog(BakerJpaController.class.getName());

	//@PersistenceContext(unitName = "bakerdb").
	
	 @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
	


	public long countInstalledBuns() {

		EntityManager entityManager = entityManagerFactory.createEntityManager();

        
		Query q = entityManager.createQuery("SELECT COUNT(s) FROM InstalledBun s") ;
		return (Long) q.getSingleResult();
	}
	
	
	public InstalledBun update(InstalledBun is) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction entityTransaction = entityManager.getTransaction();

        entityTransaction.begin();
        InstalledBun resis = entityManager.merge(is);
        entityTransaction.commit();
        
		return resis;
	}

	public void saveInstalledBun(InstalledBun is) {
    	logger.info("Will create InstalledBun = " +is.getUuid());

    	EntityManager entityManager = entityManagerFactory.createEntityManager();

        EntityTransaction entityTransaction = entityManager.getTransaction();

        entityTransaction.begin();
        
		entityManager.persist(is);
		entityManager.flush();
        entityTransaction.commit();
	}

	public InstalledBun readInstalledBunByName(final String name) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query q = entityManager.createQuery("SELECT m FROM InstalledBun m WHERE m.name='"+name+"'") ;
		return (InstalledBun) q.getSingleResult();
	}
	
	public InstalledBun readInstalledBunByUUID(final String uuid) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query q = entityManager.createQuery("SELECT m FROM InstalledBun m WHERE m.uuid='"+uuid +"'") ;
		return (InstalledBun) q.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<InstalledBun> read(int firstResult, int maxResults) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		Query q = entityManager.createQuery("SELECT m FROM InstalledBun m") ;
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
	
	
	 public void getAll() {
     	logger.info("================= getAll() ==================START");

		EntityManager entityManager = entityManagerFactory.createEntityManager();
     	
	        List<InstalledBun>lb = entityManager.createQuery( "select p from InstalledBun p").getResultList();
	        for (Iterator iterator = lb.iterator(); iterator.hasNext();) {
	        	InstalledBun iservice = (InstalledBun) iterator.next();
				logger.info("=== InstalledBun found: "+iservice.getName() +
						" Uuid: "+iservice.getUuid()+
						" RepoUrl: "+iservice.getRepoUrl()+
						" InstalledVersion: "+iservice.getInstalledVersion()+
						" Status: "+iservice.getStatus() );
				if (iservice.getBunMetadata()!=null)
					logger.info("=== InstalledBunMetadata found: "+iservice.getBunMetadata().getName() +
							" Uuid: "+iservice.getBunMetadata().getUuid()+
							" getPackageLocation: "+iservice.getBunMetadata().getPackageLocation()+
							" getVersion: "+iservice.getBunMetadata().getVersion() );
				
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
		
		Query q = entityManager.createQuery("DELETE FROM InstalledBun ") ;
		q.executeUpdate();
		entityManager.flush();
		
		entityTransaction.commit();
	}
}
