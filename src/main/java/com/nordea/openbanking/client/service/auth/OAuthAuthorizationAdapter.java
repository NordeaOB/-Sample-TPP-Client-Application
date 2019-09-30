package com.nordea.openbanking.client.service.auth;

import com.nordea.openbanking.client.client.AuthorizationClientAdapter;
import com.nordea.openbanking.client.client.VersionAdapterDispatcher;
import com.nordea.openbanking.client.session.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;

@Component
@RequiredArgsConstructor
public class OAuthAuthorizationAdapter implements AuthorizationCountryAdapter {

    private static final List<String> SUPPORTED_COUNTRIES = asList("FI", "NO", "DK");

    private final VersionAdapterDispatcher adapterDispatcher;
    private final UserSession userSession;

    @Value("${obi.oauth.callback.url}")
    private String oAuthCallbackUrl;

    @Override
    public String authorize(Model model) {
        return "redirect:" + userSession.getBaseUrl() + "/" + userSession.getApiVersion() + "/authorize?" +
                "state=web-wallet-state" +
                "&redirect_uri=" + oAuthCallbackUrl +
                "&client_id=" + userSession.getClientId() +
                "&country=" + userSession.getCountry() +
                "&duration=" + userSession.getDuration() +
                "&scope=" + String.join(",", userSession.getScopes());
    }

    @Override
    public String exchangeCodeToToken(String code) {
        return adapterDispatcher.getAdapterFor(AuthorizationClientAdapter.class).exchangeCodeToToken(code, oAuthCallbackUrl);
    }

    @Override
    public boolean isAdapterFor(String country) {
        return SUPPORTED_COUNTRIES.contains(country);
    }

    @Override
    public Optional<String> getDecoupledTokenIfReady(String orderRef, String tppToken) {
        throw new IllegalStateException("Decoupled Token exchange is not possible for OAuth flow");
    }

}
