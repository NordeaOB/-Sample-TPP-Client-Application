package com.nordea.openbanking.client.model.payments.generic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

/**
 * Creditor reference number
 */
@Data
public class Reference {
  /**
   * Type of the reference number, e.g. RF
   */
  public enum TypeEnum {
    RF("RF"),
    
    INVOICE("INVOICE"),
    
    OCR("OCR");

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

  @JsonProperty("_type")
  private TypeEnum type;

  @JsonProperty("value")
  private String value;

}

