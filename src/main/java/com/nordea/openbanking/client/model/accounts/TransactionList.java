package com.nordea.openbanking.client.model.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TransactionList {
  @JsonProperty("_links")
  private List<Link> links = new ArrayList<>();

  @JsonProperty("continuation_key")
  private String continuationKey;

  @JsonProperty("transactions")
  private List<Transaction> transactions = new ArrayList<>();

}

