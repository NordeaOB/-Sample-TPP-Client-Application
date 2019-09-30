package com.nordea.openbanking.client.client;

import com.nordea.openbanking.client.session.UserSession;
import com.nordea.openbanking.v4.auth.api.AuthorizationApi;
import com.nordea.openbanking.v4.auth.api.DecoupledAuthorizationApi;
import com.nordea.openbanking.v4.auth.model.DecoupledAuthRequestV4;
import com.nordea.openbanking.v4.auth.model.DecoupledAuthRequestV4.ScopeEnum;
import com.nordea.openbanking.v4.auth.model.DecoupledAuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static java.util.stream.Collectors.toList;

/**
 * This client adapter is for *V4* of the Authorization API
 * <p>
 * All xtra Header values are null and will be added during request @ObiHttpRequestInterceptor
 *
 * @author Jan Nielsen
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AuthorizationV4ClientAdapter implements AuthorizationClientAdapter {

    private final AuthorizationApi oAuthClientV4;
    private final DecoupledAuthorizationApi decoupledAuthClientV4;
    private final UserSession userSession;

    @Override
    public String exchangeCodeToToken(String code, String redirectUri) {
        log.debug("Calling for an access_token - using basepath {}", oAuthClientV4.getApiClient().getBasePath());
        return oAuthClientV4.accessTokenUsingPOST2(LocalDate.now().toString(), "disgest123",
                "sign123", userSession.getClientId(), userSession.getClientSecret(), userSession.getClientId(), "WebWallet", userSession.getBaseUrl(), code, redirectUri, "").getAccessToken();
    }

    @Override
    public DecoupledAuth startDecoupledAuthorization(String redirectUri) {
        DecoupledAuthRequestV4 request = new DecoupledAuthRequestV4();
        request.setPsuId(userSession.getNetbankID());
        request.setResponseType("nordea_code");
        request.language("SE");
        request.setDuration(userSession.getDuration());
        request.setState("web-wallet-state");
        request.setAccountList(userSession.getAccounts());
        request.setScope(userSession.getScopes().stream().map(ScopeEnum::valueOf).collect(toList()));
        DecoupledAuthResponse response = decoupledAuthClientV4.authorizationV4Se(LocalDate.now().toString(),
                "digest123", "sign123", userSession.getClientId(), userSession.getClientSecret(), request, userSession.getClientId(), userSession.getBaseUrl())
                .getResponse();
        return new DecoupledAuth(response.getOrderRef(), response.getTppToken());
    }

    @Override
    public String getDecoupledCode(String orderRef, String tppToken) {
        decoupledAuthClientV4.getApiClient().addDefaultHeader("Authorization", "Bearer " + tppToken);
        return decoupledAuthClientV4.getStatusUsingGET2(null,
                null, null, null, orderRef, userSession.getClientId(), userSession.getBaseUrl()).getResponse().getCode();
    }

    @Override
    public String getDecoupledToken(String code, String redirectUri) {
        return decoupledAuthClientV4.getTokenUsingPOST2(null,
                null, null, null, null, "authorization_code",
                null, null, code, "").getResponse().getAccessToken();
    }

    @Override
    public String getVersion() {
        return "V4";
    }

}
