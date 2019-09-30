package com.nordea.openbanking.client.service.accounts;

import com.nordea.openbanking.client.client.AccountsClientAdapter;
import com.nordea.openbanking.client.client.VersionAdapterDispatcher;
import com.nordea.openbanking.client.model.accounts.AccountDetails;
import com.nordea.openbanking.client.model.accounts.AccountList;
import com.nordea.openbanking.client.model.accounts.TransactionList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AccountRepository {

    private final VersionAdapterDispatcher adapterDispatcher;

    public AccountList getAccounts() {
        return adapterDispatcher.getAdapterFor(AccountsClientAdapter.class).accountList();
    }

    public AccountDetails getAccountDetails(String account) {
        return adapterDispatcher.getAdapterFor(AccountsClientAdapter.class).accountDetails(account);
    }

    public TransactionList getTransactions(String account,
                                           String continuationKey,
                                           LocalDate fromDate,
                                           String language,
                                           LocalDate toDate) {
        return adapterDispatcher.getAdapterFor(AccountsClientAdapter.class).transactionList(account, continuationKey, fromDate, language, toDate);
    }

}
