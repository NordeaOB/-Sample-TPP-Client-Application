package com.nordea.openbanking.client.client;

import com.nordea.openbanking.dynamic.sbx.model.CreateAccountRequest;

public interface DynamicSBXClientAdapter extends VersionAdapter {
    void createAccount(CreateAccountRequest createAccountRequest);
    void deleteAccount(String accountId);
    void createTransaction(String accountId);
}
