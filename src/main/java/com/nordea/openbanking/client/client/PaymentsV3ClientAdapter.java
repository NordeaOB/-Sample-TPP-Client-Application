package com.nordea.openbanking.client.client;

import com.nordea.openbanking.client.model.accounts.CountryEnum;
import com.nordea.openbanking.client.model.payments.generic.CreatePaymentRequest;
import com.nordea.openbanking.client.model.payments.generic.Link;
import com.nordea.openbanking.client.model.payments.generic.Payment;
import com.nordea.openbanking.client.model.payments.generic.PaymentList;
import com.nordea.openbanking.client.session.UserSession;
import com.nordea.openbanking.v3.pis.sepa.api.PaymentsV3Api;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

/**
 * This client adapter is fo *V3* of the payments API
 * It can take care of both SEPA and Domestic payments, FI is assumed to be SEPA, and everything else is Domestic
 *
 * all xtra Header values are null and will be added during request @ObiHttpRequestInterceptor
 * @author Jan Nielsen
 */
@Log4j2
@Component
public class PaymentsV3ClientAdapter implements PaymentsClientAdapter {

    private ModelMapper modelMapper = new ModelMapper();

    private final PaymentsV3Api clientSEPA;
    private final com.nordea.openbanking.v3.pis.domestic.api.PaymentsV3Api clientDomestic;
    private final UserSession userSession;

    public PaymentsV3ClientAdapter(PaymentsV3Api clientSEPA, com.nordea.openbanking.v3.pis.domestic.api.PaymentsV3Api clientDomestic, UserSession userSession) {
        this.clientSEPA = clientSEPA;
        this.clientDomestic = clientDomestic;
        this.userSession = userSession;

        modelMapper.createTypeMap(com.nordea.openbanking.v3.pis.sepa.model.CreatePaymentRequest.class, CreatePaymentRequest.class);
        modelMapper.createTypeMap(com.nordea.openbanking.v3.pis.sepa.model.PaymentList.class, PaymentList.class);
        modelMapper.createTypeMap(com.nordea.openbanking.v3.pis.domestic.model.CreatePaymentRequest.class, CreatePaymentRequest.class);
        modelMapper.createTypeMap(com.nordea.openbanking.v3.pis.domestic.model.PaymentList.class, PaymentList.class);
    }

    @Override
    public PaymentList paymentList() {
        PaymentList list;
        if (userSession.getCountry().equalsIgnoreCase(CountryEnum.FI.getValue())) {
            list = modelMapper.map(clientSEPA.getPaymentsUsingGET3(null, null, null, 0, null).getResponse(), PaymentList.class);
        } else {
            list = modelMapper.map(clientDomestic.getPaymentsUsingGET2(null, null, null, 0, null).getResponse(), PaymentList.class);
        }

        // Need to change version specific href to non versioned
        list.getPayments().stream()
                .forEach(payment -> Optional.ofNullable(payment.getLinks()).orElse(Collections.emptyList()).stream()
                        .forEach(this::redo_link)
                );

        return list;
    }

    private void redo_link(Link link) {
        if (!"signing".equalsIgnoreCase(link.getRel())) {
            link.setHref(link.getHref().replace("/v3", ""));
        }
    }

    @Override
    public Payment initiatePayment(CreatePaymentRequest payment) {
        //SEPA
        if (userSession.getCountry().equalsIgnoreCase(CountryEnum.FI.getValue())) {
            log.info(String.format("Hello SEPA user from %s", userSession.getCountry()));

            com.nordea.openbanking.v3.pis.sepa.model.CreatePaymentRequest createPaymentRequest = modelMapper.map(payment,
                    com.nordea.openbanking.v3.pis.sepa.model.CreatePaymentRequest.class);
            return modelMapper.map(clientSEPA.paymentInitiateUsingPOST3("", createPaymentRequest, null, null), Payment.class);
        } else {
            //Domestic
            log.info(String.format("Hello domestic user from %s", userSession.getCountry()));
            com.nordea.openbanking.v3.pis.domestic.model.CreatePaymentRequest createPaymentRequest = modelMapper.map(payment,
                    com.nordea.openbanking.v3.pis.domestic.model.CreatePaymentRequest.class);
            return modelMapper.map(clientDomestic.paymentInitiateUsingPOST2("", createPaymentRequest, null, null), Payment.class);
        }
    }

    @Override
    public Payment confirmPayment(String paymentId) {
        if (userSession.getCountry().equalsIgnoreCase(CountryEnum.FI.getValue())) {
            return modelMapper.map(clientSEPA.paymentConfirmUsingPUT3("", paymentId, null, null), Payment.class);
        } else {
            return modelMapper.map(clientDomestic.paymentConfirmUsingPUT2("", paymentId, null, null), Payment.class);
        }
    }

    @Override
    public String getVersion() {
        return "V3";
    }
}
