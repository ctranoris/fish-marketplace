package gr.upatras.ece.nam.baker.util;

import java.security.Principal;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.common.security.SimplePrincipal;
import org.apache.cxf.common.security.UsernameToken;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.security.SecurityContext;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class ShiroBasicAuthInterceptor extends AbstractPhaseInterceptor<Message> {
	private static final transient Log logger = LogFactory.getLog(ShiroBasicAuthInterceptor.class.getName());

	private ShiroUTValidator validator;

	public ShiroBasicAuthInterceptor() {
		this(Phase.UNMARSHAL);
	}

	public ShiroBasicAuthInterceptor(String phase) {
		super(phase);
	}

	public void handleMessage(Message message) throws Fault {

		Subject currentUser = SecurityUtils.getSubject();
		if (currentUser != null) {
			logger.info("handleMessage currentUser = " + currentUser.toString());
			logger.info("currentUser.getPrincipal() = " + currentUser.getPrincipal());
			logger.info("SecurityUtils.getSubject().getSession() = " + SecurityUtils.getSubject().getSession().getId() );
			logger.info("message.getId() = " + message.getId() );
			
			 // Here We are getting session from Message
		    HttpServletRequest request = (HttpServletRequest)message.get(AbstractHTTPDestination.HTTP_REQUEST);
		    HttpSession  session = request.getSession(true);
			logger.info("session.getId() = " + session.getId() );
			
			if (currentUser.getPrincipal() != null) {
				logger.info("User [" + currentUser.getPrincipal() + "] IS ALREADY logged in successfully.");
				if (currentUser.isRemembered()) {
					return;
				}
			}
		}
		AuthorizationPolicy policy = message.get(AuthorizationPolicy.class);
		if (policy == null || policy.getUserName() == null || policy.getPassword() == null) {
			String name = null;
			if (policy != null) {
				name = policy.getUserName();
			}
			String error = "No user credentials are available";
			logger.warn(error + " " + "for name: " + name);
			throw new SecurityException(error);
		}

		try {

			UsernameToken token = convertPolicyToToken(policy);

			// Credential credential = new Credential();
			// credential.setUsernametoken(token);
			//
			// RequestData data = new RequestData();
			// data.setMsgContext(message);
			Boolean succeed = validator.validate(token);
			//
			// Create a Principal/SecurityContext
			Principal p = null;
			if (succeed) {
				p = new SimplePrincipal(policy.getUserName());
			}
			// if (credential != null && credential.getPrincipal() != null) {
			// p = credential.getPrincipal();
			// } else {
			// p = new WSUsernameTokenPrincipalImpl(policy.getUserName(), false);
			// ((WSUsernameTokenPrincipalImpl)p).setPassword(policy.getPassword());
			// }
			message.put(SecurityContext.class, createSecurityContext(p));

		} catch (Exception ex) {
			throw new Fault(ex);
		}
	}

	protected UsernameToken convertPolicyToToken(AuthorizationPolicy policy) throws Exception {

		UsernameToken token = new UsernameToken(policy.getUserName(), policy.getPassword(), policy.getAuthorizationType(), false, "", "");

		return token;
	}

	protected SecurityContext createSecurityContext(final Principal p) {
		return new SecurityContext() {

			public Principal getUserPrincipal() {
				return p;
			}

			public boolean isUserInRole(String arg0) {
				return false;
			}
		};
	}

	public ShiroUTValidator getValidator() {
		return validator;
	}

	public void setValidator(ShiroUTValidator validator) {
		this.validator = validator;
	}

}
