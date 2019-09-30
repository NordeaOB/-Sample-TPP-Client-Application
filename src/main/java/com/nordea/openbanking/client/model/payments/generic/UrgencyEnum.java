package com.nordea.openbanking.client.model.payments.generic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Urgency of the payment. NB: This field is only supported for DK Domestic payments.
 */
public enum UrgencyEnum {
    STANDARD("STANDARD"),

    EXPRESS("EXPRESS"),

    SAMEDAY("SAMEDAY");

    private String value;

    UrgencyEnum(String value) {
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
    public static UrgencyEnum fromValue(String text) {
        for (UrgencyEnum b : UrgencyEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + text + "'");
    }
}
