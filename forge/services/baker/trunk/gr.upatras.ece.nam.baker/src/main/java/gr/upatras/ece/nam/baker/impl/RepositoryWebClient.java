package gr.upatras.ece.nam.baker.impl;

import java.nio.file.Path;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gr.upatras.ece.nam.baker.BakerServiceRS;
import gr.upatras.ece.nam.baker.model.IRepositoryWebClient;
import gr.upatras.ece.nam.baker.model.ServiceMetadata;

public class RepositoryWebClient implements IRepositoryWebClient {

	private static final transient Log logger = LogFactory.getLog(RepositoryWebClient.class.getName());
	@Override
	public ServiceMetadata fetchMetadata(UUID uuid, String url) {
		logger.info("fetchMetadata from: "+url+ " , for uuid="+uuid);
		return null;
	}
	@Override
	public Path fetchPackageFromLocation(UUID uuid, String packageLocation) {
		// TODO Auto-generated method stub
		return null;
	}

}
