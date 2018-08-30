package org.salgar.configuration.driver;

import org.salgar.configuration.api.ConfigurationFacade;
import org.salgar.configuration.scope.annotataion.RefreshScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.inject.Named;

@Configuration
//@DependsOn("configServerPropertySourceConfig")
@RefreshScope
public class DriverFacadeImpl implements DriverFacade {
    @Autowired
    @Named("proxyConfigurationFacade")
    ConfigurationFacade configurationFacade;

    @Value("${stage01.environment01.serviceUrl.instance01}")
    private String oneOtherProperty;

    public String driver() {
        String parameter = configurationFacade.getProperty("stage01", "environment01", "instance01", "serviceUrl");

        return parameter;
    }

    public String driver1() {
        return this.oneOtherProperty;
    }
}
