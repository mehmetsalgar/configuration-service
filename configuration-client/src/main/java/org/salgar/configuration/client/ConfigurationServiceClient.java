package org.salgar.configuration.client;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Configuration
public class ConfigurationServiceClient {
    private RestTemplate restTemplate = new RestTemplate();

    public Map<String, String> loadConfigurationValue(String url) {
        ParameterizedTypeReference<Map<String,String>> tmp = new ParameterizedTypeReference<Map<String,String>>(){ };
        ResponseEntity<Map<String, String>> configurationMap = restTemplate.exchange(url, HttpMethod.GET,
                null, tmp);

        return configurationMap.getBody();
    }
}
