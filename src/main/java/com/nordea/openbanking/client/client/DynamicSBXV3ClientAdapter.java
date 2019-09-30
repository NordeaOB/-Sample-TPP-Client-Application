package com.nordea.openbanking.client.client;

import com.nordea.openbanking.dynamic.sbx.api.DynamicSandboxApiApi;
import com.nordea.openbanking.dynamic.sbx.model.CreateAccountRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * This client adapter is for *V3* of the Dynamic Sandbox API
 * @author Jan Nielsen
 */
@Log4j2
@Component
public class DynamicSBXV3ClientAdapter implements DynamicSBXClientAdapter {

    private final com.nordea.openbanking.dynamic.sbx.api.DynamicSandboxApiApi clientDynamic;

    public DynamicSBXV3ClientAdapter(DynamicSandboxApiApi clientDynamic) {
        this.clientDynamic = clientDynamic;
    }

    @Override
    public void createAccount(CreateAccountRequest createAccountRequest) {
        clientDynamic.createAccountUsingPOST(createAccountRequest);
    }

    @Override
    public void deleteAccount(String accountId) {
        clientDynamic.deleteUserDefinedAccountUsingDELETE(accountId);
    }

    @Override
    public void createTransaction(String accountId) {
        // Intended implementation at a later stage
        throw new IllegalStateException("Intend to implement later");
    }

    @Override
    public String getVersion() {
        return "V3";
    }

}
