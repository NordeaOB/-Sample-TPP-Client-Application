package com.nordea.openbanking.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "obi")
public class EnvironmentProperties {

    private Map<String, Map<String, Environment>> environments = new HashMap<>();

    @Data
    public static class Environment {
        private String baseURL;
        private String clientId;
        private String clientSecret;
    }

}