package org.salgar.configuration.client;

import org.salgar.configuration.api.environment.Environment;
import org.salgar.configuration.api.environment.PropertySource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConfigurationServiceBootClient {
    private static final Log LOG = LogFactory.getLog(ConfigurationServiceBootClient.class);

    @Autowired
    ConfigurableEnvironment environment;

    private RestTemplate restTemplate;

    public Map<String, Object> loadConfiguration(String url) {
        RestTemplate restTemplate = this.restTemplate == null ? getSecureRestTemplate(null, null, null) : this.restTemplate;

        Environment result =  getRemoteEnvironment(restTemplate, url);
        Map<String, Object> compositeMap = new HashMap<>();

        if(result.getPropertySources() != null) {
            for (PropertySource source : result.getPropertySources()) {
                Map<String, Object> map = (Map<String, Object>) source.getSource();
                compositeMap.putAll(map);
            }
        }

        return compositeMap;
    }

    public org.springframework.core.env.PropertySource<?> locate(String url) {
        RestTemplate restTemplate = this.restTemplate == null ? getSecureRestTemplate(null, null, null) : this.restTemplate;

        Environment result =  getRemoteEnvironment(restTemplate, url);
        CompositePropertySource compositePropertySource = new CompositePropertySource("configService");

        if(result.getPropertySources() != null) {
            for (PropertySource source : result.getPropertySources()) {
                Map<String, Object> map = (Map<String, Object>) source.getSource();
                compositePropertySource.addPropertySource(new MapPropertySource(source.getName(), map));
            }
        }

        return compositePropertySource;
    }

    private Environment getRemoteEnvironment(RestTemplate restTemplate, String url) {
        String path = "/{name}/{profile}";
        String name = (String) this.environment.getPropertySources().get("bootstrap").getProperty("application.name");
        if(name == null && "".equals(name)) {
            throw new IllegalStateException("Application name by configuration can't be empty");
        }

        String profile = (String) this.environment.getPropertySources().get("bootstrap").getProperty("application.profile");
        if(profile == null || "".equals(profile)) {
            profile = "default";
        }
        Object[] args = new String[] {name, profile};
        ResponseEntity<Environment> response = null;

        try {
            response = restTemplate.exchange(url + path, HttpMethod.GET, null, Environment.class, args);
        } catch (Throwable t) {
            LOG.error(t.getMessage(), t);
        }

        return response.getBody();
    }

    private RestTemplate getSecureRestTemplate(String username, String password, String authorization) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout((60 * 1000 + 3) + 5000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        if(password != null && authorization != null) {
            throw new IllegalStateException(
                    "You must set either 'password' or 'authorization'");
        }

        if(password != null) {
            restTemplate.setInterceptors(Arrays.<ClientHttpRequestInterceptor>asList(new BasicAuthorizationInterceptor(username, password)));
        } else if(authorization != null) {
            restTemplate.setInterceptors(Arrays.<ClientHttpRequestInterceptor>asList(new GenericAuthorization(authorization)));
        }

        return restTemplate;
    }

    private static class BasicAuthorizationInterceptor implements
            ClientHttpRequestInterceptor {

        private final String username;

        private final String password;

        public BasicAuthorizationInterceptor(String username, String password) {
            this.username = username;
            this.password = (password == null ? "" : password);
        }

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                            ClientHttpRequestExecution execution) throws IOException {
            byte[] token = Base64Utils.encode((this.username + ":" + this.password).getBytes());
            request.getHeaders().add("Authorization", "Basic " + new String(token));
            return execution.execute(request, body);
        }

    }


    private static class GenericAuthorization implements
            ClientHttpRequestInterceptor {

        private final String authorizationToken;

        public GenericAuthorization(String authorizationToken) {
            this.authorizationToken = (authorizationToken == null ? "" : authorizationToken);
        }

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
                throws IOException {
            request.getHeaders().add("Authorization", authorizationToken);
            return execution.execute(request, body);
        }
    }
}
