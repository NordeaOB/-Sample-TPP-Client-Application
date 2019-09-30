package com.nordea.openbanking.client.service.auth;

import com.nordea.openbanking.client.config.EnvironmentProperties;
import com.nordea.openbanking.client.model.auth.CompleteAuthorization;
import com.nordea.openbanking.client.model.auth.StartAuthorization;
import com.nordea.openbanking.client.session.UserSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthorizationService {

    private final List<AuthorizationCountryAdapter> authorizationAdapters;
    private final EnvironmentProperties properties;
    private final UserSession userSession;

    public Set<String> getEnvironments(String clientType) {
        return properties.getEnvironments().get(clientType).keySet();
    }

    public String authorize(CompleteAuthorization completeAuthorization, Model model) {
        updateSession(completeAuthorization);
        return getAdapter().authorize(model);
    }

    public void updateSession(StartAuthorization startAuthorization) {
        setIfNotEmpty(startAuthorization.getEnvironment(), userSession::setEnvironment);
        String clientType = startAuthorization.getClientType();
        EnvironmentProperties.Environment environment = properties.getEnvironments().get(clientType).get(startAuthorization.getEnvironment());
        userSession.setBaseUrl(environment.getBaseURL());
        userSession.setClientId(environment.getClientId());
        userSession.setClientSecret(environment.getClientSecret());
        userSession.setCountry(startAuthorization.getCountry());
        userSession.setApiVersion(startAuthorization.getApiVersion());
        userSession.setEidas(startAuthorization.getEidas());
        // Override values if present
        setIfNotEmpty(startAuthorization.getBaseUrl(), userSession::setBaseUrl);
        setIfNotEmpty(startAuthorization.getClientId(), userSession::setClientId);
        setIfNotEmpty(startAuthorization.getClientSecret(), userSession::setClientSecret);
        log.debug("The Session updated with environment {} and api version {}", userSession.getEnvironment(), userSession.getApiVersion());
    }

    public void updateSession(CompleteAuthorization completeAuthorization) {
        userSession.setNetbankID(completeAuthorization.getNetbankID());
        userSession.setScopes(completeAuthorization.getScopes());
        userSession.setDuration(completeAuthorization.getDuration());
        userSession.setAccounts(completeAuthorization.getAccountNumbers());
        userSession.setFirstAuthorizer(completeAuthorization.getFirstAuthorizer());
    }

    public void exchangeCodeAndAuthorize(String code) {
        log.debug("Trying to exchange code for an access_token");
        String token = getAdapter().exchangeCodeToToken(code);
        authorizeToken(token);
    }

    public boolean authorizeDecoupledIfReady(String orderRef, String tppToken) {
        AuthorizationCountryAdapter adapter = getAdapter();
        return adapter.getDecoupledTokenIfReady(orderRef, tppToken)
                .map(token -> {
                    authorizeToken(token);
                    return true;
                })
                .orElse(false);
    }

    private void authorizeToken(String token) {
        WebWalletAuthenticatedToken authenticatedToken = new WebWalletAuthenticatedToken(token);
        SecurityContextHolder.getContext().setAuthentication(authenticatedToken);
    }

    public AuthorizationCountryAdapter getAdapter() {
        return authorizationAdapters.stream()
                .filter(adapter -> adapter.isAdapterFor(userSession.getCountry()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Can't find adapter for country: " + userSession.getCountry()));
    }

    private void setIfNotEmpty(String valueToSet, Consumer<String> consumer) {
        Optional.ofNullable(valueToSet)
                .filter(value -> !StringUtils.isEmpty(value))
                .ifPresent(consumer);
    }
}
