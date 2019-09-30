package com.nordea.openbanking.client.service.signature;

import com.nordea.openbanking.client.session.UserSession;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;

import java.net.URI;
import java.nio.charset.Charset;
import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QSealCSignatureServiceTest {

    private static final String DIGEST = "SHA-256=x74e2QL7jdTUiZfGRS9dflCfvNvigIsWvPTtzkwH0U4=";
    private QSealCSignatureService cut;
    private UserSession userSessionMock = mock(UserSession.class);
    private HttpRequest requestMock = mock(HttpRequest.class);
    Resource keyStore;

    @Before
    public void setup() {
// setup for V2
        when(userSessionMock.getBaseUrl()).thenReturn("http://localhost:7000");
        when(userSessionMock.getClientId()).thenReturn("webWallet");
        keyStore = new ClassPathResource("keystore/QSealC-NordeaDevPortal.p12");
        cut = new QSealCSignatureService(keyStore, userSessionMock);
    }

    @Test
    public void createSignatureHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Date", LocalDate.now().toString());
        httpHeaders.add("Digest", DIGEST);
        httpHeaders.add("Content-type", MediaType.APPLICATION_JSON_VALUE);
        when(requestMock.getHeaders()).thenReturn(httpHeaders);
        when(requestMock.getURI()).thenReturn(URI.create("http://localhost:8080"));
        when(requestMock.getMethod()).thenReturn(HttpMethod.GET);

        String result = cut.createSignatureHeader(requestMock);

        assertThat(result, containsString("rsa-sha256"));
    }

    @Test
    public void calculateDigest() {
        String result = cut.calculateDigest("This is a test".getBytes(Charset.defaultCharset()));

        assertThat(result, containsString("SHA-256"));
        assertThat(result, is(DIGEST));
    }
}