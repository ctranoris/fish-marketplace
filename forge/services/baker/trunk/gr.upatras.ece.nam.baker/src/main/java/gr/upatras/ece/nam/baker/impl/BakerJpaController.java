
package gr.upatras.ece.nam.baker.impl;

import gr.upatras.ece.nam.baker.model.InstalledService;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class maintains the entity manager and get a broker element from DB
 * @author ctranoris
 *
 */
public class BakerJpaController {
	

	//@PersistenceContext(unitName = "bakerdb")
	@PersistenceContext
	private EntityManager entityManager;
	

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	private static final transient Log logger = LogFactory.getLog(BakerJpaController.class.getName());

	public long countInstalledServices() {
		Query q = entityManager.createQuery("SELECT COUNT(s) FROM InstalledService s") ;
		return (Long) q.getSingleResult();
	}

	public void createInstalledService(InstalledService is) {
    	logger.info("Will create InstalledService = " +is.getUuid());
    	logger.info("Will create entityManager = " + entityManager);
		entityManager.persist(is);
		entityManager.flush();
	}

	public InstalledService readInstalledServiceByName(final String name) {
		Query q = entityManager.createQuery("SELECT m FROM InstalledService m WHERE m.name='"+name+"'") ;
		return (InstalledService) q.getSingleResult();
	}
	
	public InstalledService readInstalledServiceByUUID(final String uuid) {
		Query q = entityManager.createQuery("SELECT m FROM InstalledService m WHERE m.uuid='"+uuid +"'") ;
		return (InstalledService) q.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<InstalledService> read(int firstResult, int maxResults) {
		Query q = entityManager.createQuery("SELECT m FROM InstalledService m") ;
		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);
		return q.getResultList();
	}
	
	
	

	public InstalledService update(InstalledService is) {
    	logger.info("Will update InstalledService = " +is.getUuid());
    	logger.info("Will update entityManager = " + entityManager);
		
    	
    	
    	InstalledService resis = entityManager.merge(is);
    			
		return resis;
	}

	public void delete(final InstalledService message) {
		entityManager.remove(message);
	}
	
	
	 public void getAll() {
	        List<InstalledService>lb = entityManager.createQuery( "select p from InstalledService p").getResultList();
	        for (Iterator iterator = lb.iterator(); iterator.hasNext();) {
	        	InstalledService iservice = (InstalledService) iterator.next();
	        	logger.info("===================================");
				logger.info(" InstalledService found: "+iservice.getName() +
						" Uuid: "+iservice.getUuid()+
						" RepoUrl: "+iservice.getRepoUrl()+
						" InstalledVersion: "+iservice.getInstalledVersion()+
						" Status: "+iservice.getStatus() );
				if (iservice.getServiceMetadata()!=null)
					logger.info(" InstalledServiceMetadata found: "+iservice.getServiceMetadata().getName() +
							" Uuid: "+iservice.getServiceMetadata().getUuid()+
							" getPackageLocation: "+iservice.getServiceMetadata().getPackageLocation()+
							" getVersion: "+iservice.getServiceMetadata().getVersion() );
				
			}
	    }
	 
	
	 
	public BakerJpaController() {
		logger.info(">>>>>>>>>>>>>> BrokerServiceImpl constructor  <<<<<<<<<<<<<<<<<<");
	}
	
    public String echo(String message) {
        return "Echo processed: " + message;
        
    }

	
	public void addInstalledService(InstalledService is) {
		

		if (entityManager!=null){
			createInstalledService(is);			
			logger.info(" InstalledService created: "+is.getName());
		}else{
			logger.info("entityManager is null");
			
		}
		
	}

	public void deleteAllInstalledService() {
		Query q = entityManager.createQuery("DELETE FROM InstalledService ") ;
		q.executeUpdate();
		
		
	}
}
