
package gr.upatras.ece.nam.baker.testclasses;

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
public class BakerJpaControllerTest {
	@PersistenceContext(unitName = "bakerdb-test")
	private EntityManager entityManager;
	

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	private static final transient Log logger = LogFactory.getLog(BakerJpaControllerTest.class.getName());

	public long count() {
		Query q = entityManager.createQuery("SELECT COUNT(s) FROM InstalledService s") ;
		return (Long) q.getSingleResult();
	}

	public void create(InstalledService messageEntity) {
		entityManager.persist(messageEntity);
	}

	public InstalledService read(final String name) {
		Query q = entityManager.createQuery("SELECT m FROM InstalledService m WHERE m.name='"+name+"'") ;
		//q.setFirstResult(1);
		//comment
		return (InstalledService) q.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<InstalledService> read(int firstResult, int maxResults) {
		Query q = entityManager.createQuery("SELECT m FROM InstalledService m") ;
		q.setFirstResult(firstResult);
		q.setMaxResults(maxResults);
		return q.getResultList();
	}
	
	
	

	public InstalledService update(InstalledService messageEntity) {
		return entityManager.merge(messageEntity);
	}

	public void delete(final InstalledService message) {
		entityManager.remove(message);
	}
	
	
	 public void getAll() {
	        List<InstalledService>lb = entityManager.createQuery( "select p from InstalledService p").getResultList();
	        for (Iterator iterator = lb.iterator(); iterator.hasNext();) {
	        	InstalledService iservice = (InstalledService) iterator.next();
				logger.info(" InstalledService found: "+iservice.getName() +
						" Uuid: "+iservice.getUuid()+
						" RepoUrl: "+iservice.getRepoUrl() );
				
			}
	    }
	 
	
	 
	public BakerJpaControllerTest() {
		logger.info(">>>>>>>>>>>>>> BrokerServiceImpl constructor  <<<<<<<<<<<<<<<<<<");
	}
	
    public String echo(String message) {
        return "Echo processed: " + message;
        
    }

	
	public void addInstalledService(InstalledService bro) {
		

		if (entityManager!=null){
			create(bro);			
			logger.info(" InstalledService created: "+bro.getName());
		}else{
			logger.info("entityManager is null");
			
		}
		
	}
}
