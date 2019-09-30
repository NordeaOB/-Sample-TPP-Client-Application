package com.nordea.openbanking.client.service.auth;

import com.nordea.openbanking.client.client.AuthorizationCorporateClientAdapter;
import com.nordea.openbanking.client.client.DecoupledCorpAuth;
import com.nordea.openbanking.client.client.VersionAdapterDispatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CorporateAuthorizationAdapter implements AuthorizationCountryAdapter {

    private static final String CLIENT_TOKEN = "client_token";
    private static final String ACCESS_ID = "access_id";
    private final VersionAdapterDispatcher adapterDispatcher;

    @Value("${obi.oauth.callback.url}")
    private String oAuthCallbackUrl;

    @Override
    public String authorize(Model model) {
        DecoupledCorpAuth corpAuthResponse = adapterDispatcher.getAdapterFor(AuthorizationCorporateClientAdapter.class)
                .startDecoupledCorpAuthorization();
        String accessId = corpAuthResponse.getAccessId();
        String clientToken = "Bearer " + corpAuthResponse.getClientToken();

        addAuthorizer(accessId, clientToken);
        model.addAttribute(ACCESS_ID, accessId);
        model.addAttribute(CLIENT_TOKEN, clientToken);
        return "waitForCorpAuthorization";
    }

    private void addAuthorizer(String accessId, String clientToken) {
        adapterDispatcher.getAdapterFor(AuthorizationCorporateClientAdapter.class)
                .confirmDecoupledCorpAuthorization(clientToken, accessId);
    }

    @Override
    public Optional<String> getDecoupledTokenIfReady(String accessId, String clientToken) {
        try {
            AuthorizationCorporateClientAdapter adapter = adapterDispatcher.getAdapterFor(AuthorizationCorporateClientAdapter.class);
            String code = adapter.getDecoupledCode(accessId, clientToken);
            return Optional.of(adapter.getDecoupledToken(code, oAuthCallbackUrl));
        } catch (RestClientException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isAdapterFor(String country) {
        return "CORP".equals(country);
    }

    @Override
    public String exchangeCodeToToken(String code) {
        throw new IllegalStateException("Only Decoupled Authorization is supported for CORP");
    }
}
