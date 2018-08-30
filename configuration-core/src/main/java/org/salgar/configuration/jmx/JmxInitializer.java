package org.salgar.configuration.jmx;

import org.salgar.configuration.ConfigurationContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.export.annotation.AnnotationJmxAttributeSource;
import org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler;
import org.springframework.jmx.support.RegistrationPolicy;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class JmxInitializer {
	@Autowired
	ConfigurationContainer configurationContainer;
	
    @Bean(name = "coreRegisterer")
    public MBeanExporter getMBeanExporter() {
        MBeanExporter mBeanExporter = new MBeanExporter();
        Map<String, Object> beans = new HashMap<>();
        beans.put("configuration:name=configuration-service,type=org.salgar.ConfigurationService,artifactId=configuration-service", new ConfigurationFacadeJmx(configurationContainer));
        mBeanExporter.setBeans(beans);
        MetadataMBeanInfoAssembler metadataMBeanInfoAssembler = new MetadataMBeanInfoAssembler();
        metadataMBeanInfoAssembler.setAttributeSource(new AnnotationJmxAttributeSource());
        mBeanExporter.setAssembler(metadataMBeanInfoAssembler);
        mBeanExporter.setRegistrationPolicy(RegistrationPolicy.REPLACE_EXISTING);
        return mBeanExporter;
    }
}