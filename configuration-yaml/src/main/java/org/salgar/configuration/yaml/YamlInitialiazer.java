package org.salgar.configuration.yaml;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class YamlInitialiazer {
    private static final Log LOG = LogFactory.getLog(YamlInitialiazer.class);



    @Bean
    public YamlFileConfiguration getYamlFileConfiguration() {
        YamlFileConfiguration yamlFileConfiguration = null;
        try {
            yamlFileConfiguration = new YamlFileConfiguration(System.getProperty("org.salgar.configurationserver.propertyfile"));
        } catch (ConfigurationException e) {
            LOG.error(e.getMessage(), e);
        }
        FileChangedReloadingStrategy fileChangedReloadingStrategy = new FileChangedReloadingStrategy();
        fileChangedReloadingStrategy.setRefreshDelay(10000);
        yamlFileConfiguration.setReloadingStrategy(fileChangedReloadingStrategy);
        return yamlFileConfiguration;
    }
}