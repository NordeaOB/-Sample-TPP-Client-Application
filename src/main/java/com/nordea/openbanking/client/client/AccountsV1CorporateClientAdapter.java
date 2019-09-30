package com.nordea.openbanking.client.client;

import com.nordea.openbanking.client.model.accounts.*;
import com.nordea.openbanking.client.session.UserSession;
import com.nordea.openbanking.v1.business.ais.api.AccountsV1Api;
import com.nordea.openbanking.v1.business.ais.model.AccountDetailsResponse;
import com.nordea.openbanking.v1.business.ais.model.AccountListResponse;
import com.nordea.openbanking.v1.business.ais.model.TransactionListResponse;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.logging.log4j.util.Strings.trimToNull;

@Component
public class AccountsV1CorporateClientAdapter implements AccountsClientAdapter {

    private ModelMapper modelMapper = new ModelMapper();

    private final AccountsV1Api client;
    private final UserSession userSession;

    public AccountsV1CorporateClientAdapter(AccountsV1Api client, UserSession userSession) {
        this.client = client;
        this.userSession = userSession;

        PropertyMap<AccountListResponse, com.nordea.openbanking.v1.business.ais.model.AccountList> accountListMap = new PropertyMap<AccountListResponse, com.nordea.openbanking.v1.business.ais.model.AccountList>() {
            protected void configure() {
                map().links(source.getResponse().getLinks());
                map().accounts(source.getResponse().getAccounts());
            }
        };

        PropertyMap<com.nordea.openbanking.v1.business.ais.model.AccountInfo, AccountInfo> accountInfoMap = new PropertyMap<com.nordea.openbanking.v1.business.ais.model.AccountInfo,
                AccountInfo>() {
            protected void configure() {
                map().setAccountName(source.getAccountName());
                map().setAccountType(parseEnumOrNull(AccountTypeEnum.class, source.getAccountType()));
                map().setBookedBalance(parseBigDecimalOrNull(source.getBookedBalance()));
                map().setAvailableBalance(parseBigDecimalOrNull(source.getAvailableBalance()));
                map().setCountry(parseEnumOrNull(CountryEnum.class, source.getCountry()));
                map().setCreditLimit(parseBigDecimalOrNull(source.getCreditLimit()));
                map().setCurrency(parseEnumOrNull(CurrencyEnum.class, source.getCurrency()));
                map().setId(source.getId());
                map().setStatus(parseEnumOrNull(AccountStatusEnum.class, source.getStatus()));
            }
        };

        PropertyMap<com.nordea.openbanking.v1.business.ais.model.AccountDetailsResponse, AccountDetails> accountDetailsMap = new PropertyMap<com.nordea.openbanking.v1.business.ais.model.AccountDetailsResponse, AccountDetails>() {
            protected void configure() {
                map().setAccountName(source.getResponse().getAccountName());
                map().setAccountType(parseEnumOrNull(AccountTypeEnum.class, source.getResponse().getAccountType()));
                map().setBookedBalance(parseBigDecimalOrNull(source.getResponse().getBookedBalance()));
                map().setAvailableBalance(parseBigDecimalOrNull(source.getResponse().getAvailableBalance()));
                map().setId(source.getResponse().getId());

            }
        };

        PropertyMap<com.nordea.openbanking.v1.business.ais.model.Transaction, DebitTransaction> transactionMap = new PropertyMap<com.nordea.openbanking.v1.business.ais.model.Transaction, DebitTransaction>() {
            protected void configure() {
                map().setAmount(parseBigDecimalOrNull(source.getAmount()));
                map().setCurrency(parseEnumOrNull(CurrencyEnum.class, source.getCurrency()));
                map().setValueDate(source.getValueDate());
                map().setBookingDate(source.getBookingDate());

            }
        };

        modelMapper.addMappings(accountListMap);
        modelMapper.addMappings(accountInfoMap);
        modelMapper.addMappings(accountDetailsMap);
        modelMapper.addMappings(transactionMap);
        modelMapper.createTypeMap(com.nordea.openbanking.v1.business.ais.model.TransactionList.class, TransactionList.class);
    }

    @Override
    public AccountList accountList() {
        AccountListResponse accountListResponse = client.getAccountsListUsingGET(userSession.getClientId(), userSession.getClientSecret(), "", "");
        List<com.nordea.openbanking.client.model.accounts.AccountInfo> accountInfoList = accountListResponse.getResponse().getAccounts().stream()
                .map(accountInfo -> modelMapper.map(accountInfo, com.nordea.openbanking.client.model.accounts.AccountInfo.class))
                .collect(Collectors.toList());

        AccountList list = new AccountList();
        list.getAccounts().addAll(accountInfoList);
        // Need to change version specific href to non versioned
        list.getAccounts().stream()
                .forEach(accountInfo -> accountInfo.getLinks().stream()
                        .forEach(link -> link.setHref(link.getHref().replace("/v1", "")))
                );

        return list;
    }

    @Override
    public TransactionList transactionList(String account, String continuationKey, LocalDate fromDate, String language, LocalDate toDate) {
        TransactionListResponse transactionListResponse = client.getTransactionsUsingGET(userSession.getClientId(), userSession.getClientSecret(), account, continuationKey, fromDate, language, toDate);

        Collection<? extends Transaction> transactionList = transactionListResponse.getResponse().getTransactions().stream()
                .map(transaction -> modelMapper.map(transaction, DebitTransaction.class))
                .collect(Collectors.toList());

        TransactionList list = new TransactionList();
        list.getTransactions().addAll(transactionList);
        return list;
    }

    @Override
    public AccountDetails accountDetails(String account) {
        AccountDetailsResponse accountDetailsResponse = client.getAccountDetailsUsingGET(userSession.getClientId(), userSession.getClientSecret(), account);
        return modelMapper.map(accountDetailsResponse, AccountDetails.class);
    }

    @Override
    public String getVersion() {
        return "V1";
    }

    private static <T extends Enum<T>> T parseEnumOrNull(Class<T> clazz, String string) {
        return trimToNull(string) == null ? null : T.valueOf(clazz, string);
    }

    private static BigDecimal parseBigDecimalOrNull(String amount) {
        return amount == null ? null : new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }
}