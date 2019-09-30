package com.nordea.openbanking.client.client;

public interface AuthorizationClientAdapter extends VersionAdapter {
    String exchangeCodeToToken(String code, String redirectUri);
    DecoupledAuth startDecoupledAuthorization(String redirectUri);
    String getDecoupledCode(String orderRef, String tppToken);
    String getDecoupledToken(String code, String redirectUri);
}
