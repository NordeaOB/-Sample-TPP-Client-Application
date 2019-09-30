package com.nordea.openbanking.client.service.signature;

import com.nordea.openbanking.client.session.UserSession;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;
import org.tomitribe.auth.signatures.Signature;
import org.tomitribe.auth.signatures.Signer;

import java.net.URI;
import java.security.Key;
import java.security.KeyStore;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.Optional.ofNullable;
import static org.apache.logging.log4j.util.Strings.isNotEmpty;
import static org.springframework.http.HttpMethod.GET;

@Service
@Slf4j
public class QSealCSignatureService {

    private static final char[] STORE_PASS = "1111".toCharArray();
    private static final String[] GET_HEADERS = new String[]{"(request-target)", "host", "date"};
    private static final String[] INSERT_HEADERS = new String[]{"(request-target)", "host", "date", "content-type", "digest"};

    private final Key key;
    private final UserSession userSession;

    @SneakyThrows
    public QSealCSignatureService(@Value("classpath:keystore/QSealC-NordeaDevPortal.p12") Resource webWalletKeyStore,
                                  UserSession userSession) {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(webWalletKeyStore.getInputStream(), STORE_PASS);
        this.key = keyStore.getKey("privatekey", STORE_PASS);
        this.userSession = userSession;
    }

    public String createSignatureHeader(HttpRequest request) {
        if (request.getMethod() == GET) {
            return createGetSignatureHeader(request);
        }
        return createInsertSignature(request);
    }

    @SneakyThrows
    private String createGetSignatureHeader(HttpRequest request) {
        String path = getPath(request.getURI());
        Map<String, String> headers = createMandatoryHeaders(request);
        Signature signature = new Signature(userSession.getClientId(), "rsa-sha256", null, GET_HEADERS);
        return new Signer(key, signature).sign(request.getMethod().name(), path, headers).toString();
    }

    @SneakyThrows
    private String createInsertSignature(HttpRequest request) {
        String path = getPath(request.getURI());
        Map<String, String> headers = createMandatoryHeaders(request);
        headers.put("Digest", ofNullable(request.getHeaders().getFirst("Digest")).map(Objects::toString).orElse(null));
        headers.put("Content-type", ofNullable(request.getHeaders().getContentType()).map(Objects::toString).orElse(null));

        Signature signature = new Signature(userSession.getClientId(), "rsa-sha256", null, INSERT_HEADERS);
        return new Signer(key, signature).sign(request.getMethod().name(), path, headers).toString();
    }

    private Map<String, String> createMandatoryHeaders(HttpRequest request) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", getHost());
        headers.put("Date", request.getHeaders().getFirst("Date"));
        return headers;
    }

    public String calculateDigest(byte[] body) {
        return "SHA-256=" + new String(Base64.getEncoder().encode(DigestUtils.sha256(body)));
    }

    private String getPath(URI requestURI) {
        return requestURI.getRawPath() + getQueryIfNotEmpty(requestURI.getRawQuery());
    }

    private String getQueryIfNotEmpty(String query) {
        return isNotEmpty(query)
                ? "?" + query
                : "";
    }

    private String getHost() {
        URI baseUri = URI.create(userSession.getBaseUrl());
        return baseUri.getHost() + getPortIfRequired(baseUri.getPort());
    }

    private String getPortIfRequired(int port) {
        return port != 80
                ? ":" + port
                : "";
    }

}
