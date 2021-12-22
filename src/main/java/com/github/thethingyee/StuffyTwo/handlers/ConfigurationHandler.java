/*
    Article: How to Load Properties Using Apache Commons Configuration 2
    Publisher: JavaPointers

    Source: https://javapointers.com/how-to/how-to-load-properties-using-apache-commons-configuration-2/
 */

package com.github.thethingyee.StuffyTwo.handlers;

import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class ConfigurationHandler {

    private static ConfigurationHandler instance;
    private FileBasedConfiguration configuration;

    private ConfigurationHandler() {
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
                new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                        .configure(params.properties()
                                .setFileName("application.properties"));
        try {
            configuration = builder.getConfiguration();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static synchronized ConfigurationHandler getInstance() {
        if (instance == null) {
            instance = new ConfigurationHandler();
        }
        return instance;
    }

    public String getProperty(String key) {
        return (String) configuration.getProperty(key);
    }

    public boolean useBackslashes() {
        return configuration.getBoolean("compatibility.useBackslashes");
    }

    public boolean useAlternateURL() {
        return configuration.getBoolean("aws.embed.useAlternateURL");
    }
}
