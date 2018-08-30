package org.configuration.initialisation;


import org.salgar.configuration.ConfigurationContainer;
import org.salgar.configuration.api.ConfigurationFacade;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;

import javax.inject.Named;

public class ConfigServerPropertySourceConfig implements InitializingBean {
    @Autowired
    ConfigurableEnvironment configurableEnvironment;

    @Autowired
    @Named("proxyConfigurationFacade")
    ConfigurationFacade configurationFacade;

    @Autowired
    ConfigurationContainer configurationContainer;

    public void afterPropertiesSet() throws Exception {
        //MutablePropertySources propertySources = configurableEnvironment.getPropertySources();
        //propertySources.addFirst(getPropertySource());
    }

    private PropertySource getPropertySource() {
        return new PropertySource("configuration-server") {
            @Override
            public Object getProperty(String name) {
                if(configurationContainer != null) {
                    return configurationContainer.getProperty(name);
                }
                return configurationFacade.getProperty(name);
            }
        };
    }
}
