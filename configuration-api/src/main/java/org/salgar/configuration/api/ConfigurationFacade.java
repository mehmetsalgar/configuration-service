package org.salgar.configuration.api;

public interface ConfigurationFacade {
    String getProperty(String channel, String stage, String instance, String name);
    String getProperty(String name);
    void refresh();
}
