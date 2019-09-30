package com.nordea.openbanking.client.model.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BankInfo {

  @JsonProperty("bic")
  private BicEnum bic;

  @JsonProperty("country")
  private CountryEnum country;

  @JsonProperty("name")
  private String name;

  public BankInfo bic(BicEnum bic) {
    this.bic = bic;
    return this;
  }

}

