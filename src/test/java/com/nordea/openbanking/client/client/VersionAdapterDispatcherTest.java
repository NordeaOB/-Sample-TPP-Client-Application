package com.nordea.openbanking.client.client;

import com.nordea.openbanking.client.session.UserSession;
import com.nordea.openbanking.v2.auth.api.AuthorizationApi;
import com.nordea.openbanking.v2.auth.api.DecoupledAuthorizationApi;
import com.nordea.openbanking.v3.ais.api.CustomerAccountsApi;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VersionAdapterDispatcherTest {

    VersionAdapterDispatcher cut;
    private List<VersionAdapter> versionAdapters = new LinkedList<>();
    private UserSession userSessionMock = mock(UserSession.class);

    @Before
    public void setup() {
        cut = new VersionAdapterDispatcher(versionAdapters, userSessionMock);
    }

    @Test(expected = IllegalStateException.class)
    public void getAdapterFor_missing_list_fail() {
        cut.getAdapterFor(AccountsClientAdapter.class);
    }


    @Test
    public void getAdapterFor_AccountsV3() {
        // setup for V2
        when(userSessionMock.getApiVersion()).thenReturn("V3");

        CustomerAccountsApi clientV2Mock = mock(CustomerAccountsApi.class);
        com.nordea.openbanking.v3.ais.api.CustomerAccountsApi clientV3Mock = mock(com.nordea.openbanking.v3.ais.api.CustomerAccountsApi.class);
        VersionAdapter versionAdapterV3 = new AccountsV3ClientAdapter(clientV3Mock);
        versionAdapters.add(versionAdapterV3);
        cut = new VersionAdapterDispatcher(versionAdapters, userSessionMock);

        //Execute
        AccountsClientAdapter adapterFor = cut.getAdapterFor(AccountsClientAdapter.class);

        assertThat(adapterFor, is(notNullValue()));
        assertThat(adapterFor.getVersion(), is("V3"));
    }

    @Test
    public void getAdapterFor_AuthorizationV2() {
        // setup for V2
        when(userSessionMock.getApiVersion()).thenReturn("V2");

        AuthorizationApi clientV2Mock = mock(AuthorizationApi.class);
        com.nordea.openbanking.v3.auth.api.AuthorizationApi clientV3Mock = mock(com.nordea.openbanking.v3.auth.api.AuthorizationApi.class);
        DecoupledAuthorizationApi dclientV2Mock = mock(DecoupledAuthorizationApi.class);
        com.nordea.openbanking.v3.auth.api.DecoupledAuthorizationApi dclientV3Mock = mock(com.nordea.openbanking.v3.auth.api.DecoupledAuthorizationApi.class);
        VersionAdapter versionAdapterV2 = new AuthorizationV2ClientAdapter(clientV2Mock, dclientV2Mock, userSessionMock);
        VersionAdapter versionAdapterV3 = new AuthorizationV3ClientAdapter(clientV3Mock, dclientV3Mock, userSessionMock);
        versionAdapters.add(versionAdapterV2);
        versionAdapters.add(versionAdapterV3);
        cut = new VersionAdapterDispatcher(versionAdapters, userSessionMock);

        //Execute
        AuthorizationClientAdapter adapterFor = cut.getAdapterFor(AuthorizationClientAdapter.class);

        assertThat(adapterFor, is(notNullValue()));
        assertThat(adapterFor.getVersion(), is("V2"));
    }

    @Test
    public void getAdapterFor_AuthorizationV3() {
        // setup for V2
        when(userSessionMock.getApiVersion()).thenReturn("V3");

        AuthorizationApi clientV2Mock = mock(AuthorizationApi.class);
        com.nordea.openbanking.v3.auth.api.AuthorizationApi clientV3Mock = mock(com.nordea.openbanking.v3.auth.api.AuthorizationApi.class);
        DecoupledAuthorizationApi dclientV2Mock = mock(DecoupledAuthorizationApi.class);
        com.nordea.openbanking.v3.auth.api.DecoupledAuthorizationApi dclientV3Mock = mock(com.nordea.openbanking.v3.auth.api.DecoupledAuthorizationApi.class);
        VersionAdapter versionAdapterV2 = new AuthorizationV2ClientAdapter(clientV2Mock, dclientV2Mock, userSessionMock);
        VersionAdapter versionAdapterV3 = new AuthorizationV3ClientAdapter(clientV3Mock, dclientV3Mock, userSessionMock);
        versionAdapters.add(versionAdapterV2);
        versionAdapters.add(versionAdapterV3);
        cut = new VersionAdapterDispatcher(versionAdapters, userSessionMock);

        //Execute
        AuthorizationClientAdapter adapterFor = cut.getAdapterFor(AuthorizationClientAdapter.class);

        assertThat(adapterFor, is(notNullValue()));
        assertThat(adapterFor.getVersion(), is("V3"));
    }

}