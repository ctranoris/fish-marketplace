package gr.upatras.ece.nam.baker;

import static org.junit.Assert.*;

import java.util.UUID;

import gr.upatras.ece.nam.baker.model.InstalledService;
import gr.upatras.ece.nam.baker.model.InstalledServiceStatus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class InstalledServiceTest {


	private static final transient Log logger = LogFactory.getLog(InstalledServiceTest.class.getName());
	
	@Test
	public void testGetUuid() {
		InstalledService is = installedServiceInit();
		assertNotNull(is.getUuid());		
				
	}

	@Test
	public void testSetUuid() {
		InstalledService is = installedServiceInit();
		UUID uuid = UUID.randomUUID();
		is.setUuid(uuid);
		assertEquals(uuid, is.getUuid());
	}

	@Test
	public void testGetRepoUrl() {
		InstalledService is = installedServiceInit();
		assertNotNull(is.getRepoUrl());		
				
	}

	@Test
	public void testSetRepoUrl() {
		InstalledService is = installedServiceInit();
		String url= "testurl";
		is.setRepoUrl(url);
		assertEquals(url, is.getRepoUrl());
	}

	@Test
	public void testGetInstalledVersion() {
		InstalledService is = installedServiceInit();
		assertNotNull(is.getInstalledVersion());		
	}

	@Test
	public void testSetInstalledVersion() {
		InstalledService is = installedServiceInit();
		String version= "2.2vv2";
		is.setInstalledVersion(version);
		assertEquals(version, is.getInstalledVersion());
	}
	
	@Test
	public void testSetStatus() {
		InstalledService is = installedServiceInit();
		
		is.setStatus( InstalledServiceStatus.INSTALLING ); 
		assertEquals(InstalledServiceStatus.INSTALLING, is.getStatus());
	}
	
	
	//helpers
	private InstalledService installedServiceInit(){
		UUID uuid =  UUID.randomUUID();		
		String repoUrl="repourl";
		InstalledService is = new InstalledService(uuid , repoUrl);
		is.setInstalledVersion("1.1v");
		return is;
	}

}
