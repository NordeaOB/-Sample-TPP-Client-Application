package com.nordea.openbanking.client.client;

import com.nordea.openbanking.client.session.UserSession;
import com.nordea.openbanking.v3.auth.api.AuthorizationApi;
import com.nordea.openbanking.v3.auth.api.DecoupledAuthorizationApi;
import com.nordea.openbanking.v3.auth.model.DecoupledAuthRequestV3;
import com.nordea.openbanking.v3.auth.model.DecoupledAuthRequestV3.ScopeEnum;
import com.nordea.openbanking.v3.auth.model.DecoupledAuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static java.util.stream.Collectors.toList;

/**
 * This client adapter is for *V3* of the Authorization API
 *
 * @author Jan Nielsen
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthorizationV3ClientAdapter implements AuthorizationClientAdapter {

    private final AuthorizationApi oAuthClientV3;
    private final DecoupledAuthorizationApi decoupledAuthClientV3;
    private final UserSession userSession;

    @Override
    public String exchangeCodeToToken(String code, String redirectUri) {
        log.debug("Calling for an access_token - using basepath {}", oAuthClientV3.getApiClient().getBasePath());
        return oAuthClientV3.accessTokenUsingPOST1("authorization_code", null, null, code, redirectUri, "").getAccessToken();
    }

    @Override
    public DecoupledAuth startDecoupledAuthorization(String redirectUri) {
        DecoupledAuthRequestV3 request = new DecoupledAuthRequestV3();
        request.setPsuId(userSession.getNetbankID());
        request.setRedirectUri(redirectUri);
        request.setResponseType("nordea_code");
        request.language("SE");
        request.setDuration(userSession.getDuration());
        request.setState("web-wallet-state");
        request.setAccountList(userSession.getAccounts());
        request.setScope(userSession.getScopes().stream().map(ScopeEnum::valueOf).collect(toList()));
        DecoupledAuthResponse response = decoupledAuthClientV3.authorizationV3Se(request, null, null)
                .getResponse();
        return new DecoupledAuth(response.getOrderRef(), response.getTppToken());
    }

    @Override
    public String getDecoupledCode(String orderRef, String tppToken) {
        decoupledAuthClientV3.getApiClient().addDefaultHeader("Authorization", "Bearer " + tppToken);
        return decoupledAuthClientV3.getStatusUsingGET1(orderRef, null, null).getResponse().getCode();
    }

    @Override
    public String getDecoupledToken(String code, String redirectUri) {
        return decoupledAuthClientV3.getTokenUsingPOST1("authorization_code", null, null, code, redirectUri, "").getResponse().getAccessToken();
    }

    @Override
    public String getVersion() {
        return "V3";
    }

}
