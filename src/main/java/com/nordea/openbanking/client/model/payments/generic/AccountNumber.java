package com.nordea.openbanking.client.model.payments.generic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AccountNumber {

  @JsonProperty("_type")
  private TypeEnum type;

  @JsonProperty("currency")
  private CurrencyEnum currency;

  @JsonProperty("value")
  private String value;

}

