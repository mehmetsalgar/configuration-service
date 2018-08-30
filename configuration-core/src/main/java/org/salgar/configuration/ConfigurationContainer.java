package org.salgar.configuration;

import org.salgar.configuration.api.ConfigurationFacade;
import org.salgar.configuration.client.ConfigurationServiceBootClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Map;

@Configuration
public class ConfigurationContainer implements ConfigurationFacade {
    private volatile static ConfigurationContainer INSTANCE;
    private final static String CONFIGURATION_SERVER_URL = "configuration.server.url";

    private Map<String, Object> configurationMap = null;

    //@Autowired
    //private ConfigurationServiceClient configurationServiceClient;

    @Autowired
    private ConfigurationServiceBootClient configurationServiceBootClient;

    @Autowired
    Environment environment;

    public ConfigurationContainer() {
    }

    @Bean
    public static ConfigurationContainer getInstance() {
        if (INSTANCE == null) {
            initialize();
        }
        return INSTANCE;
    }

    private static synchronized void initialize() {
        if(INSTANCE == null) {
            INSTANCE = new ConfigurationContainer();
        }
    }

    private synchronized void initializeConfigurationMap() {
        if(this.configurationMap == null) {
            String url = this.environment.getProperty(CONFIGURATION_SERVER_URL);
            this.configurationMap = this.configurationServiceBootClient.loadConfiguration(url);
        }
    }

    public String getProperty(String name) {
        Object property = this.configurationMap.get(name);
        return property != null ? property.toString() : null;
    }

    public String getProperty(String channel, String stage, String instance, String name) {
        Object property = this.configurationMap.get(channel + "." + stage + "." + name + "." + instance);
        return property != null ? property.toString() : null;
    }

    public void refresh() {
        this.configurationMap = null;
        initializeConfigurationMap();
    }
}