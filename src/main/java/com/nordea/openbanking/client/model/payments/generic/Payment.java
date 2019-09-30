package com.nordea.openbanking.client.model.payments.generic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Payment {

  @JsonProperty("_id")
  private String id;

  @JsonProperty("_links")
  private List<Link> links = null;

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

  @JsonProperty("fee")
  private Fee fee = null;

  @JsonProperty("payment_status")
  private PaymentStatusEnum paymentStatus;

  @JsonProperty("timestamp")
  private LocalDateTime timestamp;

  @JsonProperty("urgency")
  private UrgencyEnum urgency;

}

