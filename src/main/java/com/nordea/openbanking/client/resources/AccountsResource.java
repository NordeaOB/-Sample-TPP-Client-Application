package com.nordea.openbanking.client.resources;

import com.nordea.openbanking.client.model.accounts.TransactionFilterCriteria;
import com.nordea.openbanking.client.model.accounts.TransactionList;
import com.nordea.openbanking.client.service.accounts.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/accounts")
@Controller
@RequiredArgsConstructor
public class AccountsResource {

    private static final String RESPONSEMODEL = "apiResponse";
    private final AccountRepository accountRepository;

    @GetMapping
    public String accountList(Model model) {
        model.addAttribute(RESPONSEMODEL, accountRepository.getAccounts());
        return "accountList";
    }

    @GetMapping("/{iban}")
    public String accountDetails(@PathVariable("iban") String iban, Model model) {
        model.addAttribute(RESPONSEMODEL, accountRepository.getAccountDetails(iban));
        return "accountDetails";
    }

    @GetMapping("/{iban}/transactions")
    public String listTransactions(@PathVariable("iban") String iban, Model model) {
        TransactionList apiResponse = accountRepository.getTransactions(iban, null, null, null, null);
        model.addAttribute(RESPONSEMODEL, apiResponse);
        return "transactionList";
    }

    @RequestMapping("/{iban}/filter")
    public String filterTransactions(@PathVariable("iban") String iban,
                                     @RequestBody TransactionFilterCriteria transactionFilterCriteria, Model model) {
        TransactionList apiResponse = accountRepository.getTransactions(iban, null,
                transactionFilterCriteria.getFromDate(), null, transactionFilterCriteria.getToDate());
        model.addAttribute(RESPONSEMODEL, apiResponse);
        return "transactionList";
    }

}
