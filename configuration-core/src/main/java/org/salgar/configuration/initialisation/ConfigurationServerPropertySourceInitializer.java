package org.salgar.configuration.initialisation;

import org.salgar.configuration.ConfigurationContainer;
import org.salgar.configuration.propertysource.ConfigServerPropertyResource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.MutablePropertySources;


@Configuration
public class ConfigurationServerPropertySourceInitializer implements ApplicationListener<ApplicationEvent>,
        Ordered, InitializingBean {

    @Autowired
    ConfigurableEnvironment configurableEnvironment;
    
    @Autowired
    ConfigurationContainer configurationContainer;


    private PropertySource getPropertySource() {
    	configurationContainer.refresh();
        return new ConfigServerPropertyResource(configurationContainer);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 30;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            //MutablePropertySources propertySources = configurableEnvironment.getPropertySources();
            //propertySources.addLast(getPropertySource());
        }
    }

	@Override
	public void afterPropertiesSet() throws Exception {
		MutablePropertySources propertySources = configurableEnvironment.getPropertySources();
        propertySources.addLast(getPropertySource());
	}
}
