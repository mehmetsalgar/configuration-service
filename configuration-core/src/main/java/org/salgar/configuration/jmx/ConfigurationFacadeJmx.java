package org.salgar.configuration.jmx;

import org.salgar.configuration.api.ConfigurationFacade;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(objectName = "configuration:name=configuration-service,type=org.salgar.ConfigurationService,artifactId=configuration-service")
public class ConfigurationFacadeJmx implements ConfigurationFacade {
    private ConfigurationFacade configurationFacade;

    public ConfigurationFacadeJmx(ConfigurationFacade configurationFacade) {
        this.configurationFacade = configurationFacade;
    }

    @Override
    @ManagedOperation(description = "delivers the configuration parameters")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "name", description = "Name of the configuration parameter")
    })
    public String getProperty(String name) {
        return this.configurationFacade.getProperty(name);
    }

    @Override
    @ManagedOperation(description = "delivers the configuration parameters")
    public void refresh() {
        this.configurationFacade.refresh();
    }

    @Override
    @ManagedOperation(description = "delivers the configuration parameters")
    @ManagedOperationParameters({
            @ManagedOperationParameter(name = "channel", description = "Channel for configuration values, like, PK, FIL, etc"),
            @ManagedOperationParameter(name = "stage", description = "Stage of the configuration values, like, DEV, ToC, etc"),
            @ManagedOperationParameter(name = "instance", description = "instance for the configuration values"),
            @ManagedOperationParameter(name = "name", description = "Name of the configuration parameter")
    })
    public String getProperty(String channel, String stage, String instance, String name) {
        return this.configurationFacade.getProperty(channel, stage, instance, name);
    }
}
