package com.nordea.openbanking.client.service.payments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.nordea.openbanking.client.client.PaymentsClientAdapter;
import com.nordea.openbanking.client.client.VersionAdapterDispatcher;
import com.nordea.openbanking.client.model.payments.generic.*;
import com.nordea.openbanking.client.session.UserSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PaymentRepository {
    private static final Logger log = LoggerFactory.getLogger(PaymentRepository.class);

    private final VersionAdapterDispatcher adapterDispatcher;
    private final UserSession userSession;

    public PaymentList getPaymentList() {
        return adapterDispatcher.getAdapterFor(PaymentsClientAdapter.class).paymentList();
    }

    public Payment confirmPayment(String paymentId) {
        return adapterDispatcher.getAdapterFor(PaymentsClientAdapter.class).confirmPayment(paymentId);
    }

    public void initiatePaymentSEPA(CreatePaymentRequest payment) throws IOException {
        CreatePaymentRequest enrichedPayment = mapSEPAPayment(payment);
        logPayment(enrichedPayment);
        adapterDispatcher.getAdapterFor(PaymentsClientAdapter.class).initiatePayment(enrichedPayment);
    }

    public void initiatePaymentDomestic(CreatePaymentRequest payment) throws IOException  {
        CreatePaymentRequest enrichedPayment = mapDomesticPayment(payment);
        logPayment(enrichedPayment);
        adapterDispatcher.getAdapterFor(PaymentsClientAdapter.class).initiatePayment(enrichedPayment);
    }

    private CreatePaymentRequest mapSEPAPayment(CreatePaymentRequest payment) {
        CreatePaymentRequest enrichedPayment = payment;

        enrichedPayment.getCreditor().getAccount().setCurrency(CurrencyEnum.EUR);
        enrichedPayment.getDebtor().getAccount().setCurrency(CurrencyEnum.EUR);
        enrichedPayment.getDebtor().getAccount().setType(TypeEnum.IBAN);

        return enrichedPayment;
    }

    private CreatePaymentRequest mapDomesticPayment(CreatePaymentRequest payment) {
        CreatePaymentRequest enrichedPayment = payment;
        if ( userSession.getCountry().equalsIgnoreCase("DK") )  {
            enrichedPayment.getDebtor().getAccount().setType(TypeEnum.BBAN_DK);
        } else if ( userSession.getCountry().equalsIgnoreCase("SE") ) {
            enrichedPayment.getDebtor().getAccount().setType(TypeEnum.BBAN_SE);
        } else if ( userSession.getCountry().equalsIgnoreCase("NO") ) {
            enrichedPayment.getDebtor().getAccount().setType(TypeEnum.BBAN_NO);
        }
        // Hard to say if in real scenario that would be filled by the PSU or the TPP...
        enrichedPayment.getCreditor().getAccount().setCurrency(payment.getCurrency());
        enrichedPayment.getDebtor().getAccount().setCurrency(payment.getCurrency());

        if( ! Optional.ofNullable(payment.getCreditor())
                .map(creditor -> creditor.getReference())
                .map(reference -> reference.getType())
                .isPresent() ) {
            enrichedPayment.getCreditor().setReference(null);
        }

        return enrichedPayment;
    }

    public String logPayment(CreatePaymentRequest payment) throws IOException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String paymentAsJson = ow.writeValueAsString(payment);
        log.info("Payment request: {}", paymentAsJson);
        return paymentAsJson;
    }


}
