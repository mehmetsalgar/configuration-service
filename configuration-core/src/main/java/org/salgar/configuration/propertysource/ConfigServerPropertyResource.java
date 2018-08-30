package org.salgar.configuration.propertysource;

import org.salgar.configuration.ConfigurationContainer;
import org.springframework.core.env.PropertySource;

public class ConfigServerPropertyResource extends PropertySource<ConfigurationContainer> {
    private final static String CONFIGURATION_SERVER_SOURCE_NAME = "configuration-server";

    public ConfigServerPropertyResource(String name, ConfigurationContainer source) {
        super(name, source);
    }

    public ConfigServerPropertyResource(String name) {
        super(name);
    }

    public ConfigServerPropertyResource(ConfigurationContainer source) {
        super(CONFIGURATION_SERVER_SOURCE_NAME, source);
    }

    @Override
    public Object getProperty(String name) {
        return source.getProperty(name);
    }
}
