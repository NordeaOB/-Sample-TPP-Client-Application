package com.nordea.openbanking.client.client;

import com.nordea.openbanking.client.service.signature.QSealCSignatureService;
import com.nordea.openbanking.client.session.UserSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.nordea.openbanking.client.service.auth.AuthUtil.getAuthenticatedToken;
import static java.util.Locale.US;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
@SessionScope
@RequiredArgsConstructor
public class ObiHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    private static final String X_NORDEA_ORIGINATING_USER_AGENT = "X-Nordea-Originating-User-Agent";
    private static final String X_NORDEA_ORIGINATING_USER_IP = "X-Nordea-Originating-User-Ip";
    private static final String X_IBM_CLIENT_ID = "X-IBM-Client-Id";
    private static final String X_IBM_CLIENT_SECRET = "X-IBM-Client-Secret";
    private static final String SIGNATURE = "Signature";
    private static final String DIGEST = "Digest";
    private final UserSession userSession;
    private final QSealCSignatureService qSealCSignatureService;

    @Override
    public ClientHttpResponse intercept(HttpRequest request,
                                        byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        getAuthenticatedToken().ifPresent(token -> request.getHeaders().add(AUTHORIZATION, "Bearer " + token.getObiToken()));
        List<String> clientIdHeader = request.getHeaders().get(X_IBM_CLIENT_ID);
        if (clientIdHeader == null || clientIdHeader.isEmpty()) {
            request.getHeaders().add(X_IBM_CLIENT_ID, userSession.getClientId());
        }
        request.getHeaders().add(X_IBM_CLIENT_SECRET, userSession.getClientSecret());
        request.getHeaders().add(X_NORDEA_ORIGINATING_USER_AGENT, "WebWallet");
        try {
            request.getHeaders().add(X_NORDEA_ORIGINATING_USER_IP, pullIPAddress(InetAddress.getLocalHost()));
        } catch (UnknownHostException uhe) {
            log.warn("Could not find the localhost address, using 0.0.0.0 as address for header");
            request.getHeaders().add(X_NORDEA_ORIGINATING_USER_IP, "0.0.0.0");
        }
        addDateHeader(request);
        if ("enable".equals(userSession.getEidas())) {
            if (!HttpMethod.GET.equals(request.getMethod())) {
                addDigestHeader(request, body);
            }
            addSignatureHeader(request);
        }
        return execution.execute(request, body);
    }

    private String pullIPAddress(InetAddress localHost) {
        return localHost.getHostAddress();
    }

    private void addSignatureHeader(HttpRequest request) {
        String signatureHeader = qSealCSignatureService.createSignatureHeader(request);
        request.getHeaders().add(SIGNATURE, signatureHeader);
    }

    private void addDigestHeader(HttpRequest request, byte[] body) {
        request.getHeaders().add(DIGEST, qSealCSignatureService.calculateDigest(body));
    }

    private void addDateHeader(HttpRequest request) {
        Date today = new Date();
        String stringToday = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", US).format(today);
        request.getHeaders().add("Date", stringToday);
    }

}
