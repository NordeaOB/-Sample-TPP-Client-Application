package com.nordea.openbanking.client.model.payments.generic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * List of payments
 */
@Data
public class PaymentList {

  @JsonProperty("payments")
  private List<Payment> payments = new ArrayList<>();

}

