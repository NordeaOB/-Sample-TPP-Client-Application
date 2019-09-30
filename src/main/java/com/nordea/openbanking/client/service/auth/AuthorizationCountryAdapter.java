package com.nordea.openbanking.client.service.auth;

import org.springframework.ui.Model;

import java.util.Optional;

public interface AuthorizationCountryAdapter {

    String authorize(Model model);

    String exchangeCodeToToken(String code);

    Optional<String> getDecoupledTokenIfReady(String orderRef, String tppToken);

    boolean isAdapterFor(String country);

}
