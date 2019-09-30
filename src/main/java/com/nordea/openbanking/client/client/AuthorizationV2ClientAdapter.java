package com.nordea.openbanking.client.client;

import com.nordea.openbanking.client.session.UserSession;
import com.nordea.openbanking.v2.auth.api.AuthorizationApi;
import com.nordea.openbanking.v2.auth.api.DecoupledAuthorizationApi;
import com.nordea.openbanking.v2.auth.model.BearerTokenRequest;
import com.nordea.openbanking.v2.auth.model.DecoupledAuthRequestV2;
import com.nordea.openbanking.v2.auth.model.DecoupledAuthRequestV2.ScopeEnum;
import com.nordea.openbanking.v2.auth.model.DecoupledAuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toList;

/**
 * This client adapter is fo *V2* of the Authorization API
 *
 * @author Jan Nielsen
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationV2ClientAdapter implements AuthorizationClientAdapter {

    private final AuthorizationApi oAuthClientV2;
    private final DecoupledAuthorizationApi decoupledAuthClientV2;
    private final UserSession userSession;

    @Override
    public String exchangeCodeToToken(String code, String redirectUri) {
        return oAuthClientV2.accessTokenUsingPOST(code, redirectUri, null, null).getAccessToken();
    }

    @Override
    public DecoupledAuth startDecoupledAuthorization(String redirectUri) {
        DecoupledAuthRequestV2 request = new DecoupledAuthRequestV2();
        request.setPsuId(userSession.getNetbankID());
        request.setRedirectUri(redirectUri);
        request.setResponseType("nordea_code");
        request.language("SE");
        request.setDuration(userSession.getDuration());
        request.setState("web-wallet-state");
        request.setAccountList(userSession.getAccounts());
        request.setScope(userSession.getScopes().stream().map(ScopeEnum::valueOf).collect(toList()));
        log.info("Decoupled request: {}", request);
        DecoupledAuthResponse response = decoupledAuthClientV2.authorizationV2Se(userSession.getClientId(), request, null, null)
                .getResponse();
        log.info("Decoupled response: {}", response);
        return new DecoupledAuth(response.getOrderRef(), response.getTppToken());
    }

    @Override
    public String getDecoupledCode(String orderRef, String tppToken) {
        decoupledAuthClientV2.getApiClient().addDefaultHeader("Authorization", "Bearer " + tppToken);
        String code = decoupledAuthClientV2.getStatusUsingGET(orderRef, null, null).getResponse().getCode();
        log.info("Decoupled code is not null ? ", null != code);
        return code;
    }

    @Override
    public String getDecoupledToken(String code, String redirectUri) {
        BearerTokenRequest bearerTokenRequest = new BearerTokenRequest();
        bearerTokenRequest.code(code);
//        bearerTokenRequest.setGrantType("authorization_code"); // Breaking the API V2 WTF?
        bearerTokenRequest.setRedirectUri(redirectUri);
        return decoupledAuthClientV2.getTokenUsingPOST(bearerTokenRequest, null, null).getResponse().getAccessToken();
    }

    @Override
    public String getVersion() {
        return "V2";
    }

}
