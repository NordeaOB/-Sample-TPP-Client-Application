package com.nordea.openbanking.client.service.auth;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.token.Token;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.mock;

public class WebWalletAuthenticatedTokenTest {

    Token obiTokenMock = mock(Token.class);
    WebWalletAuthenticatedToken cut;

    @Before
    public void setup() {
        cut = new WebWalletAuthenticatedToken(obiTokenMock.getExtendedInformation());
    }

    @Test
    public void getName() {
        String name = cut.getName();

        assertThat(name, is(notNullValue()));
        assertThat(name, is(equalTo("WebWallet Authenticated User")));
    }

    @Test
    public void getCredentials() {
        Object credentials = cut.getCredentials();

        assertThat(credentials, is(notNullValue()));
        assertThat(credentials, is(equalTo("N/A")));

    }

    @Test
    public void getPrincipal() {
        String principal = cut.getPrincipal();

        assertThat(principal, is(notNullValue()));
        assertThat(principal, is(equalTo("WebWallet Authenticated User")));

    }
}