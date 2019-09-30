package com.nordea.openbanking.client.model.payments.generic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Debtor of the payment
 */
@Data
public class PaymentDebtor {

  @JsonProperty("account")
  private AccountNumber account = null;

  @JsonProperty("message")
  private String message;

}

