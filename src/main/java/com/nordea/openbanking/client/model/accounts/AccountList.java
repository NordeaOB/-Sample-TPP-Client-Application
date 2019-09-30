package com.nordea.openbanking.client.model.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountList {

  @JsonProperty("_links")
  private List<Link> links = new ArrayList<>();

  @JsonProperty("accounts")
  private List<AccountInfo> accounts = new ArrayList<>();

}

