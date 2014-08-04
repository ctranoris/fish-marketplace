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

package gr.upatras.ece.nam.baker.util;

import java.io.IOException;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.common.security.SimplePrincipal;

public class BakerLoginModule implements LoginModule {

	private Subject subject;
	private CallbackHandler callbackHandler;
	private Map sharedState;
	private Map options;

	private String username;

	private SimplePrincipal userPrincipal;

	private boolean succeeded = false;
	private boolean commitSucceeded = false;
	private static final transient Log logger = LogFactory.getLog(BakerLoginModule.class.getName());

	public BakerLoginModule() {
		logger.info("Login Module - constructor called");
	}

	/**
	 * Initialize this <code>LoginModule</code>.
	 * 
	 * <p>
	 * 
	 * @param subject
	 *            the <code>Subject</code> to be authenticated.
	 *            <p>
	 * 
	 * @param callbackHandler
	 *            a <code>CallbackHandler</code> for communicating with the end user (prompting for user names and passwords, for example).
	 *            <p>
	 * 
	 * @param sharedState
	 *            shared <code>LoginModule</code> state.
	 *            <p>
	 * 
	 * @param options
	 *            options specified in the login <code>Configuration</code> for this particular <code>LoginModule</code>.
	 */
	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {

		logger.info("Login Module - initialize called");
		this.subject = subject;
		this.callbackHandler = callbackHandler;
		this.sharedState = sharedState;
		this.options = options;

		logger.info("testOption value: " + (String) options.get("testOption"));

		succeeded = false;

	}

	/**
	 * Authenticate the user by prompting for a user name and password.
	 * 
	 * <p>
	 * 
	 * @return true in all cases since this <code>LoginModule</code> should not be ignored.
	 * 
	 * @exception FailedLoginException
	 *                if the authentication fails.
	 *                <p>
	 * 
	 * @exception LoginException
	 *                if this <code>LoginModule</code> is unable to perform the authentication.
	 */
	@Override
	public boolean login() throws LoginException {
		logger.info("Login Module - login called");
		if (callbackHandler == null) {
			throw new LoginException("Oops, callbackHandler is null");
		}

		Callback[] callbacks = new Callback[2];
		callbacks[0] = new NameCallback("name:");
		callbacks[1] = new PasswordCallback("password:", false);

		try {
			callbackHandler.handle(callbacks);
		} catch (IOException e) {
			throw new LoginException("Oops, IOException calling handle on callbackHandler");
		} catch (UnsupportedCallbackException e) {
			throw new LoginException("Oops, UnsupportedCallbackException calling handle on callbackHandler");
		}

		NameCallback nameCallback = (NameCallback) callbacks[0];
		PasswordCallback passwordCallback = (PasswordCallback) callbacks[1];

		username = nameCallback.getName();
		String password = new String(passwordCallback.getPassword());
		
		//if ("myName".equals(username) && "myPassword".equals(password)) {
		if ( "myPassword".equals(password)) {
//			this.sharedState.put("javax.security.auth.login.name", username);
//			this.sharedState.put("javax.security.auth.login.password", password);
			logger.info("Success! You get to log in!");
			succeeded = true;
			return succeeded;
		} else {
			logger.info("Failure! You don't get to log in");
			succeeded = false;
			throw new FailedLoginException("Sorry! No login for you.");
		}
	}

	/**
	 * <p>
	 * This method is called if the LoginContext's overall authentication succeeded (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules
	 * succeeded).
	 * 
	 * <p>
	 * If this LoginModule's own authentication attempt succeeded (checked by retrieving the private state saved by the <code>login</code> method), then this
	 * method associates a <code>SamplePrincipal</code> with the <code>Subject</code> located in the <code>LoginModule</code>. If this LoginModule's own
	 * authentication attempted failed, then this method removes any state that was originally saved.
	 * 
	 * <p>
	 * 
	 * @exception LoginException
	 *                if the commit fails.
	 * 
	 * @return true if this LoginModule's own login and commit attempts succeeded, or false otherwise.
	 */
	@Override
	public boolean commit() throws LoginException {
		logger.info("Login Module - commit called");
		if (succeeded == false) {
			return false;
		} else {
			// add a Principal (authenticated identity)
			// to the Subject

			// assume the user we authenticated is the SimplePrincipal
			userPrincipal = new SimplePrincipal("ROLE_USER");
			logger.info("subject.getPrincipals().size() = " + subject.getPrincipals().size() );
			if("admin".equals(username))
				userPrincipal = new SimplePrincipal("ROLE_ADMIN");
			
			if (!subject.getPrincipals().contains(userPrincipal)) {
				subject.getPrincipals().add(userPrincipal);
				logger.info("added SimplePrincipal to Subject");
			}
			logger.info("subject.getPrincipals().size() = " + subject.getPrincipals().size() );
			
			

			// in any case, clean out state
			username = null;

			commitSucceeded = true;
			return true;
		}
	}

	/**
	 * <p>
	 * This method is called if the LoginContext's overall authentication failed. (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules did
	 * not succeed).
	 * 
	 * <p>
	 * If this LoginModule's own authentication attempt succeeded (checked by retrieving the private state saved by the <code>login</code> and
	 * <code>commit</code> methods), then this method cleans up any state that was originally saved.
	 * 
	 * <p>
	 * 
	 * @exception LoginException
	 *                if the abort fails.
	 * 
	 * @return false if this LoginModule's own login and/or commit attempts failed, and true otherwise.
	 */
	@Override
	public boolean abort() throws LoginException {
		logger.info("Login Module - abort called");
		return false;
	}

	/**
	 * Logout the user.
	 * 
	 * <p>
	 * This method removes the <code>SimplePrincipal</code> that was added by the <code>commit</code> method.
	 * 
	 * <p>
	 * 
	 * @exception LoginException
	 *                if the logout fails.
	 * 
	 * @return true in all cases since this <code>LoginModule</code> should not be ignored.
	 */
	@Override
	public boolean logout() throws LoginException {
		logger.info("Login Module - logout called");
		subject.getPrincipals().remove(userPrincipal);
		succeeded = false;
		succeeded = commitSucceeded;
		username = null;
		userPrincipal = null;
		return true;
	}

}
