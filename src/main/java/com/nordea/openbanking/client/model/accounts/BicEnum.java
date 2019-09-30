package com.nordea.openbanking.client.model.accounts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Business Identifier Code of account servicing institution according to ISO 9362.
 */
public enum BicEnum {
    NDEAFIHH("NDEAFIHH"),

    NDEASESS("NDEASESS"),

    NDEADKKK("NDEADKKK"),

    NDEANOKK("NDEANOKK"),

    NDEAEE2X("NDEAEE2X"),

    NDEALV2X("NDEALV2X"),

    NDEALT2X("NDEALT2X"),

    NDEAPLP2("NDEAPLP2"),

    NDEARUMM("NDEARUMM"),

    NDEALULL("NDEALULL"),

    NDEADEFF("NDEADEFF"),

    NDEAGB2L("NDEAGB2L"),

    NDEACHZZ("NDEACHZZ"),

    NDEAUS3N("NDEAUS3N"),

    NDPBSGSG("NDPBSGSG");

    private String value;

    BicEnum(String value) {
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
    public static BicEnum fromValue(String text) {
        for (BicEnum b : BicEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + text + "'");
    }
}