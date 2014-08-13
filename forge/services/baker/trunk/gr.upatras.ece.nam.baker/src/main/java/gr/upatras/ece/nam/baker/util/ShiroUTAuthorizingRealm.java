package gr.upatras.ece.nam.baker.util;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.common.security.UsernameToken;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

public class ShiroUTAuthorizingRealm extends AuthorizingRealm {

	private final List<String> requiredRoles = new ArrayList<String>();
	private static final transient Log logger = LogFactory.getLog(ShiroUTAuthorizingRealm.class.getName());


	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		logger.info("doGetAuthorizationInfo PrincipalCollection=" + arg0.toString());
		
		SimpleAuthorizationInfo ai = new SimpleAuthorizationInfo();
		ai.addRole("boss");
		return ai;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken at) throws AuthenticationException {

		logger.info("AuthenticationToken at=" + at.toString());

		UsernamePasswordToken token = (UsernamePasswordToken) at;
		logger.info("tokengetUsername at=" + token.getUsername() );
		logger.info("tokengetPassword at=" + String.valueOf(token.getPassword()) );
		logger.info("tokengetPrincipal at=" + token.getPrincipal());

//		// Validate it via Shiro
//		Subject currentUser = SecurityUtils.getSubject();
//		try {
//			currentUser.login(token);
//		} catch (AuthenticationException ex) {
//			logger.info(ex.getMessage(), ex);
//			throw new AuthenticationException("Sorry! No login for you.");
//		}
//		// Perform authorization check
//		if (!requiredRoles.isEmpty() && !currentUser.hasAllRoles(requiredRoles)) {
//			logger.info("Authorization failed for authenticated user");
//			throw new AuthenticationException("Sorry! No login for you.");
//		}
		
		SimpleAuthenticationInfo sa = new SimpleAuthenticationInfo();
		sa.setCredentials(token.getCredentials());
		SimplePrincipalCollection principals = new org.apache.shiro.subject.SimplePrincipalCollection();
		principals.add( token.getPrincipal(), "realmmmmNAMM");
		//principals.add("employee", "realmmmmNAMM");
		//principals.add("boss", "realmmmmNAMM");
		sa.setPrincipals(principals);
		return sa;
	}

	public List<String> getRequiredRoles() {
		return requiredRoles;
	}

	public void setRequiredRoles(List<String> roles) {
		requiredRoles.addAll(roles);
	}

	public boolean validate(UsernameToken usernameToken) throws LoginException {

		if (usernameToken == null) {
			throw new SecurityException("noCredential");
		}
		// Validate the UsernameToken

		String pwType = usernameToken.getPasswordType();
		logger.info("UsernameToken user " + usernameToken.getName());
		logger.info("UsernameToken password type " + pwType);

		// if (!WSConstants.PASSWORD_TEXT.equals(pwType)) {
		// if (log.isDebugEnabled()) {
		// logger.debug("Authentication failed - digest passwords are not accepted");
		// }
		// throw new WSSecurityException(WSSecurityException.ErrorCode.FAILED_AUTHENTICATION);
		// }

		if (usernameToken.getPassword() == null) {

			logger.debug("Authentication failed - no password was provided");

			throw new FailedLoginException("Sorry! No login for you.");
		}

		// Validate it via Shiro
		Subject currentUser = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(usernameToken.getName(), usernameToken.getPassword());
		token.setRememberMe(true);
		try {
			currentUser.login(token);
		} catch (AuthenticationException ex) {
			logger.info(ex.getMessage(), ex);
			throw new FailedLoginException("Sorry! No login for you.");
		}
		// Perform authorization check
		if (!requiredRoles.isEmpty() && !currentUser.hasAllRoles(requiredRoles)) {
			logger.info("Authorization failed for authenticated user");
			throw new FailedLoginException("Sorry! No login for you.");
		}

		boolean succeeded = true;

		return succeeded;
	}

}
