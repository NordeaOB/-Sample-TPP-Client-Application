package com.nordea.openbanking.client.service.auth;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthUtilTest {

    Authentication authenticationMock = mock(WebWalletAuthenticatedToken.class);
    SecurityContext securityContextMock = mock(SecurityContext.class);

    @Before
    public void setup() {
        when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        SecurityContextHolder.setContext(securityContextMock);
    }

    @Test
    public void getAuthenticatedToken() {
        Optional<WebWalletAuthenticatedToken> authenticatedToken = AuthUtil.getAuthenticatedToken();

        assertThat(authenticatedToken, is(notNullValue()));
        assertThat(authenticatedToken.get(), is(any(WebWalletAuthenticatedToken.class)));
    }
}