package com.nordea.openbanking.client.service.auth;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@UtilityClass
public class AuthUtil {

    public static Optional<WebWalletAuthenticatedToken> getAuthenticatedToken() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(authentication -> authentication instanceof  WebWalletAuthenticatedToken)
                .map(authentication -> (WebWalletAuthenticatedToken) authentication);
    }
}
