package gr.upatras.ece.nam.baker.testclasses;

import gr.upatras.ece.nam.baker.model.IRepositoryWebClient;
import gr.upatras.ece.nam.baker.model.ServiceMetadata;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MockRepositoryWebClient implements IRepositoryWebClient {

	private static final transient Log logger = LogFactory
			.getLog(MockRepositoryWebClient.class.getName());

	enum MockRepositoryBehavior {
		NORMAL, RETURN_NULLMETADATA, RETURN_NULLPACKAGEFILE

	}

	MockRepositoryBehavior mockRepositoryBehavior = MockRepositoryBehavior.NORMAL;

	public MockRepositoryWebClient(String b) {
		this.mockRepositoryBehavior = MockRepositoryBehavior.valueOf(b);

	}

	@Override
	public ServiceMetadata fetchMetadata(UUID uuid, String url) {
		logger.info("TEST fetchMetadata from: " + url + " , for uuid=" + uuid);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ServiceMetadata sm = null;
		if (mockRepositoryBehavior != MockRepositoryBehavior.RETURN_NULLMETADATA) {
			sm = new ServiceMetadata(uuid, "TemporaryServiceFromMockClass");
			sm.setPackageLocation(url + "/examplepackages/examplebun.tar.gz");
			
			sm.setVersion("1.0.0.test");
		}
		return sm;
	}

	@Override
	public Path fetchPackageFromLocation(UUID uuid, String packageLocation) {

		logger.info("TEST fetchMetadata after 2sec from: " + packageLocation);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			URL res = getClass().getResource("/files/examplebun.tar.gz");
			logger.info("TEST RESOURCE FILE: " + res );
			
			File sourceFile = new File(res.getFile());

			Path tempDir = Files.createTempDirectory("baker");
			File destFile = new File(tempDir+"/"+uuid+"/bun.tar.gz" );
			Files.createDirectory( Paths.get( tempDir+"/"+uuid ) );
			Path targetPath = destFile.toPath();

			logger.info( " to:" + targetPath);
			
			Files.copy(sourceFile.toPath(), targetPath );
			if (mockRepositoryBehavior != MockRepositoryBehavior.RETURN_NULLPACKAGEFILE) {
				return targetPath;
			}			
			

		} catch (IOException e) {

			e.printStackTrace();

		}

		

		return null;

	}
	
	

}
