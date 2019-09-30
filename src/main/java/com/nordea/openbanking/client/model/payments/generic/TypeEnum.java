package com.nordea.openbanking.client.model.payments.generic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Type of account number
 */
public enum TypeEnum {
    IBAN("IBAN"),

    BBAN_SE("BBAN_SE"),

    BBAN_DK("BBAN_DK"),

    BBAN_NO("BBAN_NO"),

    BGNR("BGNR"),

    PGNR("PGNR");

    private String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String text) {
        for (TypeEnum b : TypeEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + text + "'");
    }
}
