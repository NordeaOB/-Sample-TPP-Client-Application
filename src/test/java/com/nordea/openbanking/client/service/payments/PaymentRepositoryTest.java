package com.nordea.openbanking.client.service.payments;

import com.nordea.openbanking.client.client.PaymentsClientAdapter;
import com.nordea.openbanking.client.client.PaymentsV3ClientAdapter;
import com.nordea.openbanking.client.client.VersionAdapterDispatcher;
import com.nordea.openbanking.client.model.payments.generic.CreatePaymentRequest;
import com.nordea.openbanking.client.session.UserSession;
import com.nordea.openbanking.v3.pis.domestic.model.PaymentResponse;
import com.nordea.openbanking.v3.pis.sepa.api.PaymentsV3Api;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PaymentRepositoryTest {

    private UserSession userSessionMock = mock(UserSession.class);
    private VersionAdapterDispatcher adapterDispatcherMock = mock(VersionAdapterDispatcher.class);
    private com.nordea.openbanking.v3.pis.domestic.api.PaymentsV3Api clientDomesticMock = mock(com.nordea.openbanking.v3.pis.domestic.api.PaymentsV3Api.class);
    private PaymentsV3Api clientSepaMock = mock(PaymentsV3Api.class);

    private PaymentRepository cut;

    @Before
    public void setup() {
        when(adapterDispatcherMock.getAdapterFor(PaymentsClientAdapter.class)).thenReturn(new PaymentsV3ClientAdapter(clientSepaMock,
                clientDomesticMock, userSessionMock));
        PaymentResponse paymentDomesticResponse = new PaymentResponse();
        when(clientDomesticMock.paymentInitiateUsingPOST2(any(),any(),any(),any())).thenReturn(paymentDomesticResponse);

        com.nordea.openbanking.v3.pis.sepa.model.PaymentResponse paymentSepaResponse = new com.nordea.openbanking.v3.pis.sepa.model.PaymentResponse();
        when(clientSepaMock.paymentInitiateUsingPOST3(any(),any(),any(),any())).thenReturn(paymentSepaResponse);

        cut = new PaymentRepository(adapterDispatcherMock, userSessionMock);
    }

    @Test
    public void initiatePaymentDomestic_DK_Success() {
        when(userSessionMock.getCountry()).thenReturn("DK");
        CreatePaymentRequest request = PaymentsUtil.getDomesticDKPaymentRequest();
        try {
            cut.initiatePaymentDomestic(request);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Test
    public void initiatePaymentDomestic_SE_Success() {
        when(userSessionMock.getCountry()).thenReturn("SE");
        CreatePaymentRequest request = PaymentsUtil.getDomesticSEPaymentRequest();
        try {
            cut.initiatePaymentDomestic(request);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    @Test
    public void initiatePaymentSEPA_Success() {
        when(userSessionMock.getCountry()).thenReturn("FI");
        CreatePaymentRequest request = PaymentsUtil.getSEPAPaymentRequest();
        try {
            cut.initiatePaymentSEPA(request);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}