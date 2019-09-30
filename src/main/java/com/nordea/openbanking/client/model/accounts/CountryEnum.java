package com.nordea.openbanking.client.model.accounts;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Country code according to ISO Alpha-2
 */
public enum CountryEnum {
    FI("FI"),

    SE("SE"),

    DK("DK"),

    NO("NO"),

    DE("DE"),

    EE("EE"),

    LV("LV"),

    LT("LT"),

    PL("PL"),

    RU("RU"),

    LU("LU"),

    CH("CH"),

    US("US"),

    SG("SG"),

    GB("GB"),

    UK("UK");

    private String value;

    CountryEnum(String value) {
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
    public static CountryEnum fromValue(String text) {
        for (CountryEnum b : CountryEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + text + "'");
    }
}