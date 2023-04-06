package utilities;

import org.aeonbits.owner.ConfigCache;

public class ConfigurationManager {

    private ConfigurationManager() {
    }

    public static ConfigurationProperties getConfiguration() {
        return ConfigCache.getOrCreate(ConfigurationProperties.class);
    }
}
