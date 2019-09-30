package com.nordea.openbanking.client.client;

import com.nordea.openbanking.client.model.payments.generic.CreatePaymentRequest;
import com.nordea.openbanking.client.model.payments.generic.Payment;
import com.nordea.openbanking.client.model.payments.generic.PaymentList;

public interface PaymentsClientAdapter extends VersionAdapter {

    PaymentList paymentList();
    Payment initiatePayment(CreatePaymentRequest payment);
    Payment confirmPayment(String paymentId);
}
