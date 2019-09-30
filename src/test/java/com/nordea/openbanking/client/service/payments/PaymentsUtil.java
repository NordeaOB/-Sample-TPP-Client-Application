package com.nordea.openbanking.client.service.payments;

import com.nordea.openbanking.client.model.payments.generic.*;

import java.math.BigDecimal;

public class PaymentsUtil {

    public static CreatePaymentRequest getPaymentRequest() {
        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setAmount(BigDecimal.ONE);
        return request;
    }

    public static CreatePaymentRequest getDomesticDKPaymentRequest() {
        AccountNumber account = new AccountNumber();
        account.setCurrency(CurrencyEnum.DKK);
        account.setType(TypeEnum.BBAN_DK);
        account.setValue("1234567890");
        CreatePaymentRequest request = getPaymentRequest();
        PaymentCreditor creditor = new PaymentCreditor();
        creditor.setAccount(account);
        creditor.setMessage("Dummy");
        request.setCreditor(creditor);
        PaymentDebtor debtor = new PaymentDebtor();
        debtor.setAccount(account);
        request.setDebtor(debtor);
        request.setCurrency(CurrencyEnum.DKK);
        return request;
    }

    public static CreatePaymentRequest getDomesticSEPaymentRequest() {
        AccountNumber account = new AccountNumber();
        account.setCurrency(CurrencyEnum.SEK);
        account.setType(TypeEnum.BBAN_SE);
        account.setValue("1234567890");
        CreatePaymentRequest request = getPaymentRequest();
        PaymentCreditor creditor = new PaymentCreditor();
        creditor.setAccount(account);
        creditor.setMessage("Dummy");
        request.setCreditor(creditor);
        PaymentDebtor debtor = new PaymentDebtor();
        debtor.setAccount(account);
        request.setDebtor(debtor);
        request.setCurrency(CurrencyEnum.SEK);
        return request;
    }

    public static CreatePaymentRequest getSEPAPaymentRequest() {
        CreatePaymentRequest request = getPaymentRequest();
        PaymentCreditor creditor = new PaymentCreditor();
        AccountNumber credit_account = new AccountNumber();
        credit_account.setCurrency(CurrencyEnum.EUR);
        credit_account.setType(TypeEnum.IBAN);
        credit_account.setValue("FI3822651801064076");
        creditor.setAccount(credit_account);
        request.setCreditor(creditor);
        PaymentDebtor debtor = new PaymentDebtor();
        AccountNumber debit_account = new AccountNumber();
        debit_account.setCurrency(CurrencyEnum.EUR);
        debit_account.setType(TypeEnum.IBAN);
        debit_account.setValue("FI9310053500006009");
        debtor.setAccount(debit_account);
        request.setDebtor(debtor);
        request.setCurrency(CurrencyEnum.EUR);
        return request;
    }
}
