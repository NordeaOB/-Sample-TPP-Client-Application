package com.nordea.openbanking.client.service.accounts;

import com.nordea.openbanking.client.client.DynamicSBXClientAdapter;
import com.nordea.openbanking.client.client.VersionAdapterDispatcher;
import com.nordea.openbanking.dynamic.sbx.model.CreateAccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DynamicSBXRepository {

    private final VersionAdapterDispatcher adapterDispatcher;

    public void createAccount(CreateAccountRequest createAccountRequest) {
        adapterDispatcher.getAdapterFor(DynamicSBXClientAdapter.class).createAccount(createAccountRequest);
    }

    public void deleteAccount(String accountNumber) {
        adapterDispatcher.getAdapterFor(DynamicSBXClientAdapter.class).deleteAccount(accountNumber);
    }
}
