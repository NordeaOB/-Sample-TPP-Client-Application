package com.nordea.openbanking.client.service.auth;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static java.util.Collections.singletonList;

@EqualsAndHashCode(callSuper = true)
public class WebWalletAuthenticatedToken extends AbstractAuthenticationToken {

    @Getter
    private final String obiToken;

    public WebWalletAuthenticatedToken(String obiToken) {
        super(singletonList(new SimpleGrantedAuthority("AUTHENTICATED_ROLE")));
        this.obiToken = obiToken;
        setAuthenticated(true);
    }

    @Override
    public String getName() {
        return "WebWallet Authenticated User";
    }

    @Override
    public Object getCredentials() {
        return "N/A";
    }

    @Override
    public String getPrincipal() {
        return getName();
    }

}
