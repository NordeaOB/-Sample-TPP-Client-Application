package com.nordea.openbanking.client.model.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Link {

  @JsonProperty("href")
  private String href;

  @JsonProperty("rel")
  private String rel;

}

