package hcs.fededededed.Twitter;

import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by m13003158 on 02/04/15.
 */
public class ConfigurationFactory {

    private static ConfigurationFactory instance = null;
    private Configuration config;

    private ConfigurationFactory(){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("OmxdH8dF8PagDSO5ePpZ8mZK6")
                .setOAuthConsumerSecret("wOGjv3ePR20G7MLPUcRAqKvimWbAYdSS0umNT64PXoKDIZlwj1")
                .setOAuthAccessToken("59915564-vn1q77VqbPe6RcXZ8oLzGXN6aTIP4gSiSJw5HEbpi")
                .setOAuthAccessTokenSecret("3WJMu2FvkgzPmj8wewzVnagWwsmQr2Re3ChWYPMxIpfrO");

        config = cb.build();
    }

    public static ConfigurationFactory getInstance() {
        if (instance == null) {
            instance = new ConfigurationFactory();
        }

        return instance;
    }

    public Configuration getConfig(){
        return config;
    }
}

