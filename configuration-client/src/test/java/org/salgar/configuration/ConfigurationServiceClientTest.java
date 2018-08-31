package org.salgar.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@Test(enabled = false)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/client/applicationContext.xml"})
//, initializers = {ConfigurationServerPropertySourceInitializer.class}
public class ConfigurationServiceClientTest extends AbstractTestNGSpringContextTests {


    @Test(enabled = false)
    public void loadConfiguration() {
        //ConfigurationServiceClient configurationServiceClient = new ConfigurationServiceClient();
        //configurationServiceClient.loadConfigurationValue();
        System.out.println("test");
    }
}