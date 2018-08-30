package org.salgar.configuration.scope.jmx;

import org.salgar.configuration.scope.RefreshScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.export.annotation.AnnotationJmxAttributeSource;
import org.springframework.jmx.export.assembler.MetadataMBeanInfoAssembler;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ReshreshScopeJmxInitializer {
    @Autowired
    private RefreshScope refreshScope;

    @Bean
    public MBeanExporter getMBeanExporter() {
        MBeanExporter mBeanExporter = new MBeanExporter();
        Map<String, Object> beans = new HashMap<>();
        beans.put("configuration:name=configuration-service,type=org.salgar.scope.RefreshScope,artifactId=configuration-service", new RefreshScopeFacadeJmx(refreshScope));
        mBeanExporter.setBeans(beans);
        MetadataMBeanInfoAssembler metadataMBeanInfoAssembler = new MetadataMBeanInfoAssembler();
        metadataMBeanInfoAssembler.setAttributeSource(new AnnotationJmxAttributeSource());
        mBeanExporter.setAssembler(metadataMBeanInfoAssembler);
        return mBeanExporter;
    }
}
