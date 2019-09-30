package com.nordea.openbanking.client.client;

import com.nordea.openbanking.client.model.accounts.AccountDetails;
import com.nordea.openbanking.client.model.accounts.AccountList;
import com.nordea.openbanking.client.model.accounts.TransactionList;

import java.time.LocalDate;

public interface AccountsClientAdapter extends VersionAdapter {
    AccountList accountList();
    TransactionList transactionList(String account, String continuationKey, LocalDate fromDate, String language, LocalDate toDate);
    AccountDetails accountDetails(String account);
}
