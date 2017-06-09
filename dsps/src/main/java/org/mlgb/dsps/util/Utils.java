package org.mlgb.dsps.util;

import java.util.Properties;

/**
 * Common tools.
 * @author Leo
 *
 */
public class Utils {
    /**
     * Load configuration file.
     * @param configFile
     * @return Properties Configuration object.
     */
    public static Properties loadConfigProperties(String configFile) {
        Properties configs = new Properties();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            configs.load(classLoader.getResourceAsStream(configFile));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        return configs;
    }
}
