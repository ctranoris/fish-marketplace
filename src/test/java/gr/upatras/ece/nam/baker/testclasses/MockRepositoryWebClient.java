package gr.upatras.ece.nam.baker.testclasses;

import gr.upatras.ece.nam.baker.model.IRepositoryWebClient;
import gr.upatras.ece.nam.baker.model.ServiceMetadata;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
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
		logger.info("TEST fetchMetadata after 2sec from: " + url + " uuid="
				+ uuid);

		try {
			Thread.sleep(2000);
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
	public String fetchPackageFromLocation(UUID uuid, String packageLocation) {

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
						
			extractPackage(targetPath);
			

		} catch (IOException e) {

			e.printStackTrace();

		}

		if (mockRepositoryBehavior != MockRepositoryBehavior.RETURN_NULLPACKAGEFILE) {
			return "/tmp/baker/examplebun.tar.gz";
		}

		return null;

	}
	
	public int extractPackage(Path targetPath) throws ExecuteException, IOException{
		
		String cmdStr="tar --strip-components=1 -xvzf "+targetPath+" -C " + targetPath.getParent()+"/";
		logger.info( " Execute :" + cmdStr);
		
		CommandLine cmdLine = CommandLine.parse(cmdStr);
		// create the executor and consider the exitValue '0' as success
		final Executor executor = new DefaultExecutor();
		executor.setExitValue(0);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(out);
        executor.setStreamHandler(streamHandler);
		

		int exitValue = executor.execute(cmdLine);
		logger.info( "out>" + out);
		
		return exitValue;
		
	}

}
