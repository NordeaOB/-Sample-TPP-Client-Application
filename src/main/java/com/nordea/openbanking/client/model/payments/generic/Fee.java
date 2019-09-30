package com.nordea.openbanking.client.model.payments.generic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;

/**
 * An amount the bank will charge for executing the payment
 */
@Data
public class Fee {
  @JsonProperty("_type")
  private String type;

  /**
   * Country code according to ISO Alpha-2
   */
  public enum CountryCodeEnum {
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

    CountryCodeEnum(String value) {
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
    public static CountryCodeEnum fromValue(String text) {
      for (CountryCodeEnum b : CountryCodeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + text + "'");
    }
  }

  @JsonProperty("country_code")
  private CountryCodeEnum countryCode;

  /**
   * Currency code according to ISO 4217
   */
  public enum CurrencyCodeEnum {
    AED("AED"),
    
    AFN("AFN"),
    
    ALL("ALL"),
    
    AMD("AMD"),
    
    ANG("ANG"),
    
    AOA("AOA"),
    
    ARS("ARS"),
    
    AUD("AUD"),
    
    AWG("AWG"),
    
    AZN("AZN"),
    
    BAM("BAM"),
    
    BBD("BBD"),
    
    BDT("BDT"),
    
    BGN("BGN"),
    
    BHD("BHD"),
    
    BIF("BIF"),
    
    BMD("BMD"),
    
    BND("BND"),
    
    BOB("BOB"),
    
    BOV("BOV"),
    
    BRL("BRL"),
    
    BSD("BSD"),
    
    BTN("BTN"),
    
    BWP("BWP"),
    
    BYN("BYN"),
    
    BYR("BYR"),
    
    BZD("BZD"),
    
    CAD("CAD"),
    
    CDF("CDF"),
    
    CHE("CHE"),
    
    CHF("CHF"),
    
    CHW("CHW"),
    
    CLF("CLF"),
    
    CLP("CLP"),
    
    CNY("CNY"),
    
    COP("COP"),
    
    COU("COU"),
    
    CRC("CRC"),
    
    CUC("CUC"),
    
    CUP("CUP"),
    
    CVE("CVE"),
    
    CZK("CZK"),
    
    DJF("DJF"),
    
    DKK("DKK"),
    
    DOP("DOP"),
    
    DZD("DZD"),
    
    EGP("EGP"),
    
    ERN("ERN"),
    
    ETB("ETB"),
    
    EUR("EUR"),
    
    FJD("FJD"),
    
    FKP("FKP"),
    
    GBP("GBP"),
    
    GEL("GEL"),
    
    GHS("GHS"),
    
    GIP("GIP"),
    
    GMD("GMD"),
    
    GNF("GNF"),
    
    GTQ("GTQ"),
    
    GYD("GYD"),
    
    HKD("HKD"),
    
    HNL("HNL"),
    
    HRK("HRK"),
    
    HTG("HTG"),
    
    HUF("HUF"),
    
    IDR("IDR"),
    
    ILS("ILS"),
    
    INR("INR"),
    
    IQD("IQD"),
    
    IRR("IRR"),
    
    ISK("ISK"),
    
    JMD("JMD"),
    
    JOD("JOD"),
    
    JPY("JPY"),
    
    KES("KES"),
    
    KGS("KGS"),
    
    KHR("KHR"),
    
    KMF("KMF"),
    
    KPW("KPW"),
    
    KRW("KRW"),
    
    KWD("KWD"),
    
    KYD("KYD"),
    
    KZT("KZT"),
    
    LAK("LAK"),
    
    LBP("LBP"),
    
    LKR("LKR"),
    
    LRD("LRD"),
    
    LSL("LSL"),
    
    LYD("LYD"),
    
    MAD("MAD"),
    
    MDL("MDL"),
    
    MGA("MGA"),
    
    MKD("MKD"),
    
    MMK("MMK"),
    
    MNT("MNT"),
    
    MOP("MOP"),
    
    MRO("MRO"),
    
    MUR("MUR"),
    
    MVR("MVR"),
    
    MWK("MWK"),
    
    MXN("MXN"),
    
    MXV("MXV"),
    
    MYR("MYR"),
    
    MZN("MZN"),
    
    NAD("NAD"),
    
    NGN("NGN"),
    
    NIO("NIO"),
    
    NOK("NOK"),
    
    NPR("NPR"),
    
    NZD("NZD"),
    
    OMR("OMR"),
    
    PAB("PAB"),
    
    PEN("PEN"),
    
    PGK("PGK"),
    
    PHP("PHP"),
    
    PKR("PKR"),
    
    PLN("PLN"),
    
    PYG("PYG"),
    
    QAR("QAR"),
    
    RON("RON"),
    
    RSD("RSD"),
    
    RUB("RUB"),
    
    RWF("RWF"),
    
    SAR("SAR"),
    
    SBD("SBD"),
    
    SCR("SCR"),
    
    SDG("SDG"),
    
    SEK("SEK"),
    
    SGD("SGD"),
    
    SHP("SHP"),
    
    SLL("SLL"),
    
    SOS("SOS"),
    
    SRD("SRD"),
    
    SSP("SSP"),
    
    STD("STD"),
    
    SVC("SVC"),
    
    SYP("SYP"),
    
    SZL("SZL"),
    
    THB("THB"),
    
    TJS("TJS"),
    
    TMT("TMT"),
    
    TND("TND"),
    
    TOP("TOP"),
    
    TRY("TRY"),
    
    TTD("TTD"),
    
    TWD("TWD"),
    
    TZS("TZS"),
    
    UAH("UAH"),
    
    UGX("UGX"),
    
    USD("USD"),
    
    USN("USN"),
    
    UYI("UYI"),
    
    UYU("UYU"),
    
    UZS("UZS"),
    
    VEF("VEF"),
    
    VND("VND"),
    
    VUV("VUV"),
    
    WST("WST"),
    
    XAF("XAF"),
    
    XAG("XAG"),
    
    XAU("XAU"),
    
    XBA("XBA"),
    
    XBB("XBB"),
    
    XBC("XBC"),
    
    XBD("XBD"),
    
    XCD("XCD"),
    
    XDR("XDR"),
    
    XOF("XOF"),
    
    XPD("XPD"),
    
    XPF("XPF"),
    
    XPT("XPT"),
    
    XSU("XSU"),
    
    XTS("XTS"),
    
    XUA("XUA"),
    
    XXX("XXX"),
    
    YER("YER"),
    
    ZAR("ZAR"),
    
    ZMW("ZMW"),
    
    ZWL("ZWL");

    private String value;

    CurrencyCodeEnum(String value) {
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
    public static CurrencyCodeEnum fromValue(String text) {
      for (CurrencyCodeEnum b : CurrencyCodeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + text + "'");
    }
  }

  @JsonProperty("currency_code")
  private CurrencyCodeEnum currencyCode;

  @JsonProperty("value")
  private Double value;

  public Fee type(String type) {
    this.type = type;
    return this;
  }

   /**
   * Example &#39;domestic_transaction&#39; only for DK domestic payments
   * @return type
  **/
  @ApiModelProperty(value = "Example 'domestic_transaction' only for DK domestic payments")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Fee countryCode(CountryCodeEnum countryCode) {
    this.countryCode = countryCode;
    return this;
  }

   /**
   * Country code according to ISO Alpha-2
   * @return countryCode
  **/
  @ApiModelProperty(value = "Country code according to ISO Alpha-2")
  public CountryCodeEnum getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(CountryCodeEnum countryCode) {
    this.countryCode = countryCode;
  }

  public Fee currencyCode(CurrencyCodeEnum currencyCode) {
    this.currencyCode = currencyCode;
    return this;
  }

   /**
   * Currency code according to ISO 4217
   * @return currencyCode
  **/
  @ApiModelProperty(value = "Currency code according to ISO 4217")
  public CurrencyCodeEnum getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(CurrencyCodeEnum currencyCode) {
    this.currencyCode = currencyCode;
  }

  public Fee value(Double value) {
    this.value = value;
    return this;
  }

   /**
   * Value of the fee.
   * @return value
  **/
  @ApiModelProperty(value = "Value of the fee.")
  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Fee fee = (Fee) o;
    return Objects.equals(this.type, fee.type) &&
        Objects.equals(this.countryCode, fee.countryCode) &&
        Objects.equals(this.currencyCode, fee.currencyCode) &&
        Objects.equals(this.value, fee.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, countryCode, currencyCode, value);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Fee {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    countryCode: ").append(toIndentedString(countryCode)).append("\n");
    sb.append("    currencyCode: ").append(toIndentedString(currencyCode)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

