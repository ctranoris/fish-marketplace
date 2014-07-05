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

/**
 * A test repository for mocking a repo
 * @author ctranoris
 * 
 */
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
			if (url.contains("EBUNID") )
				sm.setPackageLocation("/files/examplebun.tar.gz");
			else if (url.contains("EBUNERR"))
				sm.setPackageLocation("/files/examplebunErrInstall.tar.gz");
			
			sm.setVersion("1.0.0.test");
		}
		return sm;
	}

	@Override
	public Path fetchPackageFromLocation(UUID uuid, String packageLocation) {

		logger.info("TEST fetchMetadata after 2sec from (dummy): " + packageLocation);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			URL res = getClass().getResource(packageLocation );
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
