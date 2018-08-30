package org.salgar.configuration.scope;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RefreshScopeInitializer {
    @Bean
    public static RefreshScope refreshScope() {
        return new RefreshScope();
    }
}
