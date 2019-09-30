package com.nordea.openbanking.client.model.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DebitTransaction extends Transaction {
  @JsonProperty("amount")
  private BigDecimal amount;

  @JsonProperty("balance_after_transaction")
  private BigDecimal balanceAfterTransaction;

  @JsonProperty("booking_date")
  private LocalDate bookingDate;

  @JsonProperty("card_number")
  private String cardNumber;

  @JsonProperty("counterparty_account")
  private String counterpartyAccount;

  @JsonProperty("counterparty_name")
  private String counterpartyName;

  @JsonProperty("currency")
  private CurrencyEnum currency;

  @JsonProperty("currency_rate")
  private BigDecimal currencyRate;

  @JsonProperty("message")
  private String message;

  @JsonProperty("narrative")
  private String narrative;

  @JsonProperty("original_currency")
  private CurrencyEnum originalCurrency;

  @JsonProperty("original_currency_amount")
  private BigDecimal originalCurrencyAmount;

  @JsonProperty("own_message")
  private String ownMessage;

  @JsonProperty("payment_date")
  private LocalDate paymentDate;

  @JsonProperty("reference")
  private String reference;

  @JsonProperty("status")
  private StatusEnum status;

  @JsonProperty("transaction_date")
  private LocalDate transactionDate;

  @JsonProperty("transaction_id")
  private String transactionId;

  @JsonProperty("type_description")
  private String typeDescription;

  @JsonProperty("value_date")
  private LocalDate valueDate;

}

