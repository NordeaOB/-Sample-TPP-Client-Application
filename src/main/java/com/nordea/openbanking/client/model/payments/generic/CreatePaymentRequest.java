package com.nordea.openbanking.client.model.payments.generic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePaymentRequest {

  @JsonProperty("amount")
  private BigDecimal amount;

  @JsonProperty("creditor")
  private PaymentCreditor creditor = null;

  @JsonProperty("currency")
  private CurrencyEnum currency;

  @JsonProperty("debtor")
  private PaymentDebtor debtor = null;

  @JsonProperty("external_id")
  private String externalId;

  @JsonProperty("urgency")
  private UrgencyEnum urgency;

}

