package com.nordea.openbanking.client.model.payments.generic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Currency of the payment
 */
public enum CurrencyEnum {
    DKK("DKK"),

    SEK("SEK"),

    NOK("NOK"),

    EUR("EUR");

    private String value;

    CurrencyEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static CurrencyEnum fromValue(String text) {
        for (CurrencyEnum b : CurrencyEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + text + "'");
    }
}