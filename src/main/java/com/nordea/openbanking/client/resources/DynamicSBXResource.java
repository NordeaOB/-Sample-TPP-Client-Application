package com.nordea.openbanking.client.resources;


import com.nordea.openbanking.client.model.accounts.AccountList;
import com.nordea.openbanking.client.service.accounts.AccountRepository;
import com.nordea.openbanking.client.service.accounts.DynamicSBXRepository;
import com.nordea.openbanking.client.session.UserSession;
import com.nordea.openbanking.dynamic.sbx.model.AccountNumber;
import com.nordea.openbanking.dynamic.sbx.model.CreateAccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/dynamic/sbx/accounts")
@Controller
@RequiredArgsConstructor
public class DynamicSBXResource {

    private static final String ACCOUNTLIST_URI = "accountList";
    private static final String RESPONSE_MODEL_NAME = "apiResponse";
    private final AccountRepository accountRepository;
    private final DynamicSBXRepository dynamicSBXRepository;
    private final UserSession userSession;

    @GetMapping("/viewDynamicSBX")
    public String showActions(Model model) {
        return "dynamicSBX";
    }

    @GetMapping("/viewCreateAccount")
    public String viewCreateAccount(final Principal principal, Model model) {

        AccountList accounts = accountRepository.getAccounts();
        model.addAttribute("currencies", CreateAccountRequest.CurrencyEnum.values());
        model.addAttribute("accountTypes", CreateAccountRequest.AccountTypeEnum.values());
        model.addAttribute("apiResponseAccounts", accounts.getAccounts());
        // Preparing the request up front
        CreateAccountRequest preparedRequest = new CreateAccountRequest();
        preparedRequest.setProduct("Brukskonto");
        setCurrency(preparedRequest);

        preparedRequest.setCountry(CreateAccountRequest.CountryEnum.fromValue(userSession.getCountry()));
        List<AccountNumber> accountList = new ArrayList<>();
        prepareAccountNumberTypes(accountList);
        preparedRequest.setAccountNumbers(accountList);
        preparedRequest.setStatus(CreateAccountRequest.StatusEnum.OPEN);

        model.addAttribute("account", preparedRequest);

        return "accountCreate";
    }

    /**
     * Setup the two account numbers with type of IBAN and BBAN
     * @param accountList
     */
    private void prepareAccountNumberTypes(List<AccountNumber> accountList) {
        for (int i = 1; i <= 2; i++) {
            AccountNumber accountNumber = new AccountNumber();
            if (i == 1) {
                accountNumber.setType(AccountNumber.TypeEnum.IBAN);
            } else {
                switch(userSession.getCountry()) {
                    case "SE":
                        accountNumber.setType(AccountNumber.TypeEnum.BBAN_SE);
                        break;
                    case "DK":
                        accountNumber.setType(AccountNumber.TypeEnum.BBAN_DK);
                        break;
                    case "NO":
                        accountNumber.setType(AccountNumber.TypeEnum.BBAN_NO);
                        break;
                    case "FI":
                    default:
                        accountNumber.setType(AccountNumber.TypeEnum.IBAN);
                }
            }
            accountList.add(accountNumber);
        }
    }

    /**
     * Set the currency based on the country of the user session
     * @param preparedRequest
     */
    private void setCurrency(CreateAccountRequest preparedRequest) {
        switch(userSession.getCountry()) {
            case "SE":
                preparedRequest.setCurrency(CreateAccountRequest.CurrencyEnum.SEK);
                break;
            case "DK":
                preparedRequest.setCurrency(CreateAccountRequest.CurrencyEnum.DKK);
                break;
            case "NO":
                preparedRequest.setCurrency(CreateAccountRequest.CurrencyEnum.NOK);
                break;
            case "FI":
            default:
                preparedRequest.setCurrency(CreateAccountRequest.CurrencyEnum.EUR);
        }
    }

    @PostMapping("/create")
    public String createAccount(final Principal principal, @ModelAttribute CreateAccountRequest account, Model model) {
        dynamicSBXRepository.createAccount(account);
        model.addAttribute(RESPONSE_MODEL_NAME, accountRepository.getAccounts());
        return ACCOUNTLIST_URI;
    }


    @GetMapping("/viewDeleteAccount")
    public String showAccountList(Model model) {
        model.addAttribute(RESPONSE_MODEL_NAME, accountRepository.getAccounts());
        model.addAttribute("deleteAction", true);
        return ACCOUNTLIST_URI;
    }

    @PostMapping("/delete/{accountNumber}")
    public String deleteAccount(@PathVariable String accountNumber, Model model) {
        dynamicSBXRepository.deleteAccount(accountNumber);
        model.addAttribute(RESPONSE_MODEL_NAME, accountRepository.getAccounts());
        return ACCOUNTLIST_URI;
    }
}
