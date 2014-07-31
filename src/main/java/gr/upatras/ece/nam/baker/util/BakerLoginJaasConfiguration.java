package gr.upatras.ece.nam.baker.util;

import java.util.Collections;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;


public class BakerLoginJaasConfiguration  extends Configuration {
	

    public AppConfigurationEntry[]  getAppConfigurationEntry(String name) {
        return new AppConfigurationEntry[] {
            new AppConfigurationEntry(BakerLoginModule.class.getName(),
                                      AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
                                      Collections.<String, String>emptyMap())
        };
    }

}
