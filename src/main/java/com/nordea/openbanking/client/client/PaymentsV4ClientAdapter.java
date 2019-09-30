package com.nordea.openbanking.client.client;

import com.nordea.openbanking.client.model.accounts.CountryEnum;
import com.nordea.openbanking.client.model.payments.generic.CreatePaymentRequest;
import com.nordea.openbanking.client.model.payments.generic.Payment;
import com.nordea.openbanking.client.model.payments.generic.PaymentList;
import com.nordea.openbanking.client.session.UserSession;
import com.nordea.openbanking.v4.pis.sepa.api.PaymentsV4Api;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

/**
 * This client adapter is fo *V4* of the payments API
 * It can take care of both SEPA and Domestic payments, FI is assumed to be SEPA, and everything else is Domestic
 *
 * all xtra Header values are null and will be added during request @ObiHttpRequestInterceptor
 * @author Jan Nielsen
 */
@Log4j2
@Component
public class PaymentsV4ClientAdapter implements PaymentsClientAdapter {

    private ModelMapper modelMapper = new ModelMapper();

    private final PaymentsV4Api clientSEPA;
    private final com.nordea.openbanking.v4.pis.domestic.api.PaymentsV4Api clientDomestic;
    private final UserSession userSession;

    public PaymentsV4ClientAdapter(PaymentsV4Api clientSEPA, com.nordea.openbanking.v4.pis.domestic.api.PaymentsV4Api clientDomestic, UserSession userSession) {
        this.clientSEPA = clientSEPA;
        this.clientDomestic = clientDomestic;
        this.userSession = userSession;

        modelMapper.createTypeMap(com.nordea.openbanking.v4.pis.sepa.model.CreatePaymentRequest.class, CreatePaymentRequest.class);
        modelMapper.createTypeMap(com.nordea.openbanking.v4.pis.sepa.model.PaymentList.class, PaymentList.class);
        modelMapper.createTypeMap(com.nordea.openbanking.v4.pis.domestic.model.CreatePaymentRequest.class, CreatePaymentRequest.class);
        modelMapper.createTypeMap(com.nordea.openbanking.v4.pis.domestic.model.PaymentList.class, PaymentList.class);
    }

    @Override
    public PaymentList paymentList() {
        PaymentList list;
        if (userSession.getCountry().equalsIgnoreCase(CountryEnum.FI.getValue())) {
            list =
                    modelMapper.map(clientSEPA.getPaymentsUsingGET5(null, null, null,
                            null,
                            null, null, null, 0, null).getResponse(),
                            PaymentList.class);
        } else {
            list = modelMapper.map(clientDomestic.getPaymentsUsingGET4(null, null, null,
                    null,
                    null, null,
                    null, 0, null).getResponse(), PaymentList.class);
        }

        // Need to change version specific href to non versioned
        list.getPayments().stream()
                .forEach(payment -> Optional.ofNullable(payment.getLinks()).orElse(Collections.emptyList())
                        .forEach(link -> link.setHref(link.getHref().replace("/v4", "")))
                );

        return list;
    }

    @Override
    public Payment initiatePayment(CreatePaymentRequest payment) {
        //SEPA
        if (userSession.getCountry().equalsIgnoreCase(CountryEnum.FI.getValue())) {
            log.info(String.format("Hello SEPA user from %s", userSession.getCountry()));

            com.nordea.openbanking.v4.pis.sepa.model.CreatePaymentRequest createPaymentRequest = modelMapper.map(payment,
                    com.nordea.openbanking.v4.pis.sepa.model.CreatePaymentRequest.class);
            return modelMapper.map(clientSEPA.paymentInitiateUsingPOST5(null, null, null,
                    null,
                    null, "", createPaymentRequest, null, null), Payment.class);
        } else {
            //Domestic
            log.info(String.format("Hello domestic user from %s", userSession.getCountry()));
            com.nordea.openbanking.v4.pis.domestic.model.CreatePaymentRequest createPaymentRequest = modelMapper.map(payment,
                    com.nordea.openbanking.v4.pis.domestic.model.CreatePaymentRequest.class);
            return modelMapper.map(clientDomestic.paymentInitiateUsingPOST4(null, null, null,
                    null,
                    null, "", createPaymentRequest, null, null), Payment.class);
        }
    }

    @Override
    public Payment confirmPayment(String paymentId) {
        if (userSession.getCountry().equalsIgnoreCase(CountryEnum.FI.getValue())) {
            return modelMapper.map(clientSEPA.paymentConfirmUsingPUT5(null, null, null,
                    null,
                    null, "", paymentId, null, null), Payment.class);
        } else {
            return modelMapper.map(clientDomestic.paymentConfirmUsingPUT4(null, null, null,
                    null,
                    null, "", paymentId, null, null), Payment.class);
        }
    }

    @Override
    public String getVersion() {
        return "V4";
    }
}
