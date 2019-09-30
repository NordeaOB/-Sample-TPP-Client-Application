package com.nordea.openbanking.client.client;

public interface AuthorizationCorporateClientAdapter extends AuthorizationClientAdapter {
    DecoupledCorpAuth startDecoupledCorpAuthorization();
    DecoupledCorpAuthStatus confirmDecoupledCorpAuthorization(String clientToken, String accessId);
}
