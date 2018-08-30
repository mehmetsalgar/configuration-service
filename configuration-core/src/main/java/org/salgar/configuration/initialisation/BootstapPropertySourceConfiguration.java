package org.salgar.configuration.initialisation;

import org.salgar.configuration.yaml.YamlFileConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Configuration
public class BootstapPropertySourceConfiguration implements ApplicationListener<ApplicationEvent>,
        Ordered, InitializingBean {
    @Autowired
    ConfigurableEnvironment configurableEnvironment;

    @Autowired
    YamlFileConfiguration yamlFileConfiguration;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            //MutablePropertySources propertySources = configurableEnvironment.getPropertySources();
            //propertySources.addFirst(new MapPropertySource("bootstrap", prepareConfigurationMap()));
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 20;
    }

    private Map<String, Object> prepareConfigurationMap() {
        Iterator<String> it = yamlFileConfiguration.getKeys();

        Map<String, Object> result = new HashMap<>();
        while (it.hasNext()) {
            String key = it.next();

            result.put(key, yamlFileConfiguration.getProperty(key));
        }

        return result;
    }

	@Override
	public void afterPropertiesSet() throws Exception {
		MutablePropertySources propertySources = configurableEnvironment.getPropertySources();
        propertySources.addFirst(new MapPropertySource("bootstrap", prepareConfigurationMap()));
	}
}