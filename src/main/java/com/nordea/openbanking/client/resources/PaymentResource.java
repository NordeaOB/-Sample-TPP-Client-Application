package com.nordea.openbanking.client.resources;

import com.nordea.openbanking.client.model.accounts.AccountList;
import com.nordea.openbanking.client.model.payments.generic.*;
import com.nordea.openbanking.client.service.accounts.AccountRepository;
import com.nordea.openbanking.client.service.payments.PaymentRepository;
import com.nordea.openbanking.client.session.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

/**
 * Takes care of all the requests from the UI for payments
 *
 * @author Jan Nielsen
 */
@RequestMapping("/payments")
@Controller
@RequiredArgsConstructor
public class PaymentResource {

    private static final String PAYMENTLIST_URI = "paymentList";
    private static final String RESPONSE_MODEL_NAME = "paymentList";
    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;
    private final UserSession userSession;

    @GetMapping({"/showCreatePayment"})
    public String showCreatePayment(Model model) {

        AccountList accounts = accountRepository.getAccounts();
        model.addAttribute("apiResponseAccounts", accounts.getAccounts());
        Payment preparedPayment = new Payment();
        setCurrency(preparedPayment);
        model.addAttribute("payment", preparedPayment);
        model.addAttribute("currencies", CurrencyEnum.values());
        model.addAttribute("urgencies", UrgencyEnum.values());

        return "paymentCreate";
    }

    @PostMapping("/create")
    public String initiatePayment(final Principal principal, @ModelAttribute CreatePaymentRequest payment, Model model) throws IOException {
        if (userSession.getCountry().equals(Fee.CountryCodeEnum.NO.getValue()) || userSession.getCountry().equals(Fee.CountryCodeEnum.SE.getValue())) {
            if (payment.getCreditor().getMessage().equals("")) {
                payment.getCreditor().setMessage(null);
            } else {
                payment.getCreditor().getReference().setValue(null);
                payment.getCreditor().getReference().setType(null);
            }
        }

        if (userSession.getCountry().equalsIgnoreCase(Fee.CountryCodeEnum.FI.getValue())) {
            paymentRepository.initiatePaymentSEPA(payment);

        } else {
            paymentRepository.initiatePaymentDomestic(payment);
        }

        model.addAttribute(RESPONSE_MODEL_NAME, paymentRepository.getPaymentList().getPayments());
        return PAYMENTLIST_URI;
    }

    @GetMapping("/showPaymentList")
    public String showPaymentList(Model model) {
        model.addAttribute(RESPONSE_MODEL_NAME, paymentRepository.getPaymentList().getPayments());
        return PAYMENTLIST_URI;
    }

    /**
     * This could be a POST method too, but since the ui issues a GET, I decided to stay with GET
     */
    @GetMapping(value = {"/domestic/{paymentId}/confirm", "/sepa/{paymentId}/confirm"})
    public String confirmPayment(@PathVariable String paymentId, Model model) {
        paymentRepository.confirmPayment(paymentId);
        model.addAttribute(RESPONSE_MODEL_NAME, paymentRepository.getPaymentList().getPayments());
        return PAYMENTLIST_URI;
    }

    /* Utility methods */

    /**
     * Set the currency based on the country of the user session
     * @param preparedRequest
     */
    private void setCurrency(Payment preparedRequest) {
        switch(userSession.getCountry()) {
            case "SE":
                preparedRequest.setCurrency(com.nordea.openbanking.client.model.payments.generic.CurrencyEnum.SEK);
                break;
            case "DK":
                preparedRequest.setCurrency(com.nordea.openbanking.client.model.payments.generic.CurrencyEnum.DKK);
                break;
            case "NO":
                preparedRequest.setCurrency(com.nordea.openbanking.client.model.payments.generic.CurrencyEnum.NOK);
                break;
            case "FI":
            default:
                preparedRequest.setCurrency(com.nordea.openbanking.client.model.payments.generic.CurrencyEnum.EUR);
        }
    }
}
