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

import java.util.Collections;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class BakerLoginJaasConfiguration  extends Configuration {
	

	private static final transient Log logger = LogFactory.getLog(BakerLoginJaasConfiguration.class.getName());
	
	
    public AppConfigurationEntry[]  getAppConfigurationEntry(String name) {
    	
    	logger.info(" == String name= " +name);
    	
        return new AppConfigurationEntry[] {
            new AppConfigurationEntry(BakerLoginModule.class.getName(),
                                      AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
                                      Collections.<String, String>emptyMap())
        };
    }

}
