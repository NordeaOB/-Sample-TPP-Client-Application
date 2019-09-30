package com.nordea.openbanking.client.session;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * An obvious indifferent test
 */
public class UserSessionTest {

    UserSession cut;

    @Before
    public void setup() {
        cut = new UserSession();
    }

    @Test
    public void null_toString_success() {
        String output = cut.toString();

        assertThat(output, is(notNullValue()));
        assertThat(output, equalTo("UserSession(environment=null, baseUrl=null, clientId=null, clientSecret=null, country=null, " +
                "apiVersion=null, eidas=null, netbankID=null, firstAuthorizer=null, duration=0, scopes=[], accounts=[])"));
    }

    @Test
    public void all_toString_success() {
        addData();

        String output = cut.toString();

        assertThat(output, is(notNullValue()));
        assertThat(output, equalTo("UserSession(environment=local, baseUrl=baseUrl, clientId=clientId, clientSecret=clientSecret, " +
                "country=FI, apiVersion=V4, eidas=asdkjhfapseiuf98yf9ds, netbankID=netbankId, firstAuthorizer=someone, duration=1234, " +
                "scopes=[ACCOUNTS_BASIC], accounts=[FI1234567901234567-EUR])"));

    }

    @Test
    public void null_hashCode_success() {
        int output = cut.hashCode();

        assertThat(output, is(notNullValue()));
        assertThat(output, equalTo(-195492002));
    }

    @Test
    public void all_hashCode_scucces() {
        addData();

        int output = cut.hashCode();

        assertThat(output, is(notNullValue()));
        assertThat(output, equalTo(-998964046));
    }

    /* ---------------------------------------------------------------------------------------------------------- */

    /**
     * Add some data, not at all descriptive
     */
    private void addData() {
        cut.setBaseUrl("baseUrl");
        cut.setClientId("clientId");
        cut.setClientSecret("clientSecret");
        List<String> accounts = new LinkedList<String>() {{
            add("FI1234567901234567-EUR");
        }};
        cut.setAccounts(accounts);
        cut.setApiVersion("V4");
        cut.setDuration(1234);
        cut.setCountry("FI");
        cut.setEnvironment("local");
        cut.setEidas("asdkjhfapseiuf98yf9ds");
        cut.setFirstAuthorizer("someone");
        cut.setNetbankID("netbankId");
        List<String> scopes = new LinkedList<String>() {{
            add("ACCOUNTS_BASIC");
        }};
        cut.setScopes(scopes);
    }
}