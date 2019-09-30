package com.nordea.openbanking.client.service.auth;

import com.nordea.openbanking.client.client.AuthorizationClientAdapter;
import com.nordea.openbanking.client.client.DecoupledAuth;
import com.nordea.openbanking.client.client.VersionAdapterDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DecoupledAuthorizationAdapter implements AuthorizationCountryAdapter {

    private final VersionAdapterDispatcher adapterDispatcher;

    @Value("${obi.oauth.callback.url}")
    private String oAuthCallbackUrl;

    @Override
    public String authorize(Model model) {
        DecoupledAuth decoupledAuthResponse = adapterDispatcher.getAdapterFor(AuthorizationClientAdapter.class)
                .startDecoupledAuthorization(oAuthCallbackUrl);
        log.info("Model comes back with some data response {}", decoupledAuthResponse);
        model.addAttribute("orderRef", decoupledAuthResponse.getOrderRef());
        model.addAttribute("tppToken", decoupledAuthResponse.getTppToken());
        return "waitForAuthorization";
    }

    @Override
    public Optional<String> getDecoupledTokenIfReady(String orderRef, String tppToken) {
        try {
            AuthorizationClientAdapter adapter = adapterDispatcher.getAdapterFor(AuthorizationClientAdapter.class);
            String code = adapter.getDecoupledCode(orderRef, tppToken);
            return Optional.of(adapter.getDecoupledToken(code, oAuthCallbackUrl));
        } catch (RestClientException e) {
            return Optional.empty();
        }
    }
    @Override
    public boolean isAdapterFor(String country) {
        return "SE".equals(country);
    }

    @Override
    public String exchangeCodeToToken(String code) {
        throw new IllegalStateException("Only Decoupled Authorization is supported for SE");
    }

}
