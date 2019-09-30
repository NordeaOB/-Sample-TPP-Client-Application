package com.nordea.openbanking.client.client;

import com.nordea.openbanking.client.model.accounts.*;
import com.nordea.openbanking.v3.ais.api.CustomerAccountsApi;
import com.nordea.openbanking.v3.ais.model.AccountDetailsResponse;
import com.nordea.openbanking.v3.ais.model.AccountInfo;
import com.nordea.openbanking.v3.ais.model.AccountListResponse;
import com.nordea.openbanking.v3.ais.model.TransactionListResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountsV3ClientAdapter implements AccountsClientAdapter {

    private ModelMapper modelMapper = new ModelMapper();

    private final CustomerAccountsApi client;

    public AccountsV3ClientAdapter(CustomerAccountsApi client) {
        this.client = client;

        PropertyMap<AccountListResponse, com.nordea.openbanking.v3.ais.model.AccountList> accountListMap = new PropertyMap<AccountListResponse, com.nordea.openbanking.v3.ais.model.AccountList>() {
            protected void configure() {
                map().accounts(source.getResponse().getAccounts());
            }
        };

        PropertyMap<AccountInfo, com.nordea.openbanking.v3.ais.model.AccountInfo> accountInfoMap = new PropertyMap<AccountInfo,
                com.nordea.openbanking.v3.ais.model.AccountInfo>() {
            protected void configure() {
                map().accountName(source.getAccountName());
                map().accountNumbers(source.getAccountNumbers());
                map().accountType(source.getAccountType());
                map().bank(source.getBank());
                map().bookedBalance(source.getBookedBalance());
                map().availableBalance(source.getAvailableBalance());
                map().country(source.getCountry());
                map().creditLimit(source.getCreditLimit());
                map().currency(source.getCurrency());
            }
        };

        modelMapper.addMappings(accountListMap);
        modelMapper.addMappings(accountInfoMap);
        modelMapper.createTypeMap(com.nordea.openbanking.v3.ais.model.TransactionList.class, TransactionList.class);
        modelMapper.createTypeMap(com.nordea.openbanking.v3.ais.model.Transaction.class, Transaction.class);
        modelMapper.createTypeMap(com.nordea.openbanking.v3.ais.model.DebitTransaction.class, DebitTransaction.class);
        modelMapper.createTypeMap(com.nordea.openbanking.v3.ais.model.CreditTransaction.class, CreditTransaction.class);
        modelMapper.createTypeMap(com.nordea.openbanking.v3.ais.model.AccountDetails.class, AccountDetails.class);
    }

    @Override
    public AccountList accountList() {
        AccountListResponse accountListResponse = client.accountListUsingGET1(null, null);
        List<com.nordea.openbanking.client.model.accounts.AccountInfo> accountInfoList = accountListResponse.getResponse().getAccounts().stream()
                .map(accountInfo -> modelMapper.map(accountInfo, com.nordea.openbanking.client.model.accounts.AccountInfo.class))
                .collect(Collectors.toList());

        AccountList list = new AccountList();
        list.getAccounts().addAll(accountInfoList);

        // Need to change version specific href to non versioned
        list.getAccounts().stream()
                        .forEach(accountInfo -> accountInfo.getLinks().stream()
                                .forEach(link -> link.setHref(link.getHref().replace("/v3", "")))
                        );

        return list;
    }

    @Override
    public TransactionList transactionList(String account, String continuationKey, LocalDate fromDate, String language, LocalDate toDate) {
        TransactionListResponse transactionListResponse = client.transactionListUsingGET1(account, null, null, continuationKey, fromDate, language, toDate);

        Collection<? extends Transaction> transactionList = transactionListResponse.getResponse().getTransactions().stream()
                .map(transaction -> {
                    Transaction mapped;
                    if (transaction instanceof com.nordea.openbanking.v3.ais.model.DebitTransaction) {
                        mapped = modelMapper.map(transaction, DebitTransaction.class);
                    } else {
                        mapped = modelMapper.map(transaction, CreditTransaction.class);
                    }
                    return mapped;
                })
                .collect(Collectors.toList());

        TransactionList list = new TransactionList();
        list.getTransactions().addAll(transactionList);
        return list;
    }

    @Override
    public AccountDetails accountDetails(String account) {
        AccountDetailsResponse accountDetailsResponse = client.accountDetailsUsingGET1(account, null, null);
        return modelMapper.map(accountDetailsResponse.getResponse(), AccountDetails.class);
    }

    @Override
    public String getVersion() {
        return "V3";
    }
}
