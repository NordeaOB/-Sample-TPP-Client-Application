package com.nordea.openbanking.client.client;

import com.nordea.openbanking.client.session.UserSession;
import com.nordea.openbanking.v1.business.auth.api.AuthorizeV1Api;
import com.nordea.openbanking.v1.business.auth.model.AddAuthorizerRequest;
import com.nordea.openbanking.v1.business.auth.model.AuthorizationStatusResponse;
import com.nordea.openbanking.v1.business.auth.model.InitialAuthorizationRequest;
import com.nordea.openbanking.v1.business.auth.model.InitialAuthorizationResponse;
import com.nordea.openbanking.v2.auth.api.AuthorizationApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationV1CorporateClientAdapter implements AuthorizationCorporateClientAdapter {

    private final AuthorizationApi oAuthClientV2;
    private final AuthorizeV1Api corpAuthClientV1;
    private final UserSession userSession;

    @Override
    public DecoupledCorpAuth startDecoupledCorpAuthorization() {
        InitialAuthorizationRequest request = new InitialAuthorizationRequest();
        request.setDuration(userSession.getDuration());
        request.setScope(userSession.getScopes().stream().map(InitialAuthorizationRequest.ScopeEnum::valueOf).collect(toList()));
        InitialAuthorizationResponse response = corpAuthClientV1.authorizationV1Baa(userSession.getClientId(), userSession.getClientSecret(), request);
        return new DecoupledCorpAuth(response.getResponse().getAccessId(), response.getResponse().getClientToken(), response.getResponse().getStatus());
    }

    @Override
    public String getDecoupledCode(String accessId, String clientToken) {
        String code = corpAuthClientV1.getStatusUsingGET(clientToken, userSession.getClientId(), userSession.getClientSecret(), accessId).getResponse().getCode();
        log.info("Decoupled code is not null ? ", null != code);
        return code;
    }

    public DecoupledCorpAuthStatus confirmDecoupledCorpAuthorization(String clientToken, String accessId) {
        AddAuthorizerRequest request = new AddAuthorizerRequest();
        request.setAuthorizerId(userSession.getFirstAuthorizer());
        AuthorizationStatusResponse response = corpAuthClientV1.addAuthorizerUsingPUT(clientToken, userSession.getClientId(), userSession.getClientSecret(), accessId, request);
        return new DecoupledCorpAuthStatus(response.getResponse().getStatus(), null);
    }

    @Override
    public String getDecoupledToken(String code, String redirectUri) {
        return corpAuthClientV1.getAccessTokenUsingPOST(userSession.getClientId(), userSession.getClientSecret(), code, "authorization_code", "").
                getResponse().getAccessToken();
    }

    @Override
    public String exchangeCodeToToken(String code, String redirectUri) {
        return oAuthClientV2.accessTokenUsingPOST(code, redirectUri, "WebWallet", userSession.getBaseUrl()).getAccessToken();
    }

    @Override
    public String getVersion() {
        return "v1";
    }

    @Override
    public DecoupledAuth startDecoupledAuthorization(String redirectUri) {
        return null;
    }
}
