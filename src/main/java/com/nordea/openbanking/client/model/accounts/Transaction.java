package com.nordea.openbanking.client.model.accounts;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.annotations.ApiModel;

/**
 * Transaction details
 */
@ApiModel(description = "Transaction details")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "_type", visible = true)
@JsonSubTypes({
  @JsonSubTypes.Type(value = CreditTransaction.class, name = "CreditTransaction"),
  @JsonSubTypes.Type(value = DebitTransaction.class, name = "DebitTransaction"),
})
public class Transaction {

}

