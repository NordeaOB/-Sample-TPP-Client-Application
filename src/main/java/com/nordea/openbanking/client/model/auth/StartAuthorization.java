package com.nordea.openbanking.client.model.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StartAuthorization {
    private String clientType;
    private String environment;
    private String baseUrl;
    private String clientId;
    private String clientSecret;
    private String country;
    private String apiVersion;
    private String eidas;
}
