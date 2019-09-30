package com.nordea.openbanking.client.model.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class AccountDetails {
  @JsonProperty("_id")
  private String id;

  @JsonProperty("_links")
  private List<Link> links = null;

  @JsonProperty("account_name")
  private String accountName;

  @JsonProperty("account_numbers")
  private List<AccountNumber> accountNumbers = new ArrayList<>();

  @JsonProperty("account_type")
  private AccountTypeEnum accountType;

  @JsonProperty("available_balance")
  private BigDecimal availableBalance;

  @JsonProperty("bank")
  private BankInfo bank = null;

  @JsonProperty("booked_balance")
  private BigDecimal bookedBalance;

}

