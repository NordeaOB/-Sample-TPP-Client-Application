package com.nordea.openbanking.client.session;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Data
@SessionScope
@Component
@RequiredArgsConstructor
public class UserSession {

    private String environment;
    private String baseUrl;
    private String clientId;
    private String clientSecret;
    private String country;
    private String apiVersion;
    private String eidas;
    private String netbankID;
    private String firstAuthorizer;
    private int duration;
    private List<String> scopes = new ArrayList<>();
    private List<String> accounts = new ArrayList<>();

}
