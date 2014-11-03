package gr.upatras.ece.nam.baker.repo;

import gr.upatras.ece.nam.baker.util.OAuthClientUtils;
import gr.upatras.ece.nam.baker.util.OAuthClientUtils.Consumer;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.rs.security.oauth2.common.ClientAccessToken;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeGrant;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants;

public class OAuthClientManager {

	private static final transient Log logger = LogFactory.getLog(OAuthClientManager.class.getName());

	private static final String DEFAULT_CLIENT_ID = "1328";
	private static final String DEFAULT_CLIENT_SECRET = "017bf9efbd5cedb0556d7e3fde2dc977650b2cefa235b8eb06bf094cc6ba26b6ab31ebd7af74f28e0ef368ae992acada34eb19401fa914cf676b45932ff68670";

	private WebClient accessTokenService;
	private String authorizationServiceURI;
	private Consumer consumer = new Consumer(DEFAULT_CLIENT_ID, DEFAULT_CLIENT_SECRET);

	// public static String getFIWAREOAUTH2URL(String redirectURI){
	// return "/oauth2/authorize?response_type=code&"+
	// "client_id="+DEFAULT_CLIENT_ID+"&"+
	// "state=xyz&"+
	// "redirect_uri="+redirectURI;
	//
	// }

	public OAuthClientManager() {

	}

	// inject properties, register the client application...

	public URI getAuthorizationServiceURI(URI redirectUri,
	/* state */String reservationRequestKey) {

		String scope = null;
		return OAuthClientUtils.getAuthorizationURI(authorizationServiceURI, consumer.getKey(), redirectUri.toString(), reservationRequestKey, scope);
	}

	public ClientAccessToken getAccessToken(AuthorizationCodeGrant codeGrant) {
		try {
			
			
			return OAuthClientUtils.getAccessToken(accessTokenService, consumer, codeGrant);
		} catch (OAuthServiceException ex) {
			return null;
		}
	}

	public String createAuthorizationHeader(ClientAccessToken token) {
		return OAuthClientUtils.createAuthorizationHeader(token);
	}

	public void setAccessTokenService(WebClient ats) {
		this.accessTokenService = ats;
	}


	public void setAuthorizationURI(String uri) {
		this.authorizationServiceURI = uri;
	}

}
