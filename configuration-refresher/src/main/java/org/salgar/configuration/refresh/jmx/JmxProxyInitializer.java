package org.salgar.configuration.refresh.jmx;

import org.salgar.configuration.api.ConfigurationFacade;
import org.salgar.configuration.api.RefreshScopeFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.access.MBeanProxyFactoryBean;

import javax.management.MalformedObjectNameException;

@Configuration
public class JmxProxyInitializer {

    @Bean(name = "proxyRefreshScopeFacade")
    public MBeanProxyFactoryBean getProxyRefreshScopeFacade() throws MalformedObjectNameException {
        MBeanProxyFactoryBean mBeanProxyFactoryBean = new MBeanProxyFactoryBean();
        mBeanProxyFactoryBean.setObjectName("configuration:name=configuration-service,type=org.salgar.scope.RefreshScope,artifactId=configuration-service");
        mBeanProxyFactoryBean.setProxyInterface(RefreshScopeFacade.class);

        return mBeanProxyFactoryBean;
    }

    @Bean(name = "proxyConfigurationFacade")
    public MBeanProxyFactoryBean getMBeanProxyFactoryBean() throws MalformedObjectNameException {
        MBeanProxyFactoryBean mBeanProxyFactoryBean = new MBeanProxyFactoryBean();
        mBeanProxyFactoryBean.setObjectName("configuration:name=configuration-service,type=org.salgar.ConfigurationService,artifactId=configuration-service");
        mBeanProxyFactoryBean.setProxyInterface(ConfigurationFacade.class);

        return mBeanProxyFactoryBean;
    }
}
