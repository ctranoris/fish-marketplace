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

package gr.upatras.ece.nam.baker.fiware;


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
import org.apache.cxf.rs.security.oauth2.client.OAuthClientUtils;
import org.apache.cxf.rs.security.oauth2.client.OAuthClientUtils.Consumer;
import org.apache.cxf.rs.security.oauth2.common.ClientAccessToken;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeGrant;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants;

public class OAuthClientManager {

	private static final transient Log logger = LogFactory.getLog(OAuthClientManager.class.getName());

//	private static final String DEFAULT_CLIENT_ID = "1334";
//	private static final String DEFAULT_CLIENT_SECRET = "ba167ecff73cf999e250413aae19b682cec475c310c12ad4e1c7689b358b1d793caaa3c5a34d38544b0317a3902438efb7204dd71c7c4c6ff790a4ff529af450";

	//THESE ARE USED FOR TESTING INTERNAL WITH LOCALHOST
	private static final String DEFAULT_CLIENT_ID = "1352";
	private static final String DEFAULT_CLIENT_SECRET = "6e664fe26ce6573b91def132d82af7141fe039d255d50fdd742aac8dd2e94f1459132e694521f2769ea211e981594c410274b072d4fc32b0d0c028880d91a8c6";

	
	
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
