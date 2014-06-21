package gr.upatras.ece.nam.baker.impl;

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
		logger.info("fetchMetadata from: "+url+ " uuid="+uuid);
		return null;
	}
	@Override
	public String fetchPackageFromLocation(UUID uuid, String packageLocation) {
		// TODO Auto-generated method stub
		return null;
	}

}
