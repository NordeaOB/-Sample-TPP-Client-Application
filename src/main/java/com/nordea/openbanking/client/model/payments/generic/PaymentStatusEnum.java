package com.nordea.openbanking.client.model.payments.generic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Status of the payment
 */
public enum PaymentStatusEnum {
    PENDINGCONFIRMATION("PendingConfirmation"),

    PENDINGUSERAPPROVAL("PendingUserApproval"),

    ONHOLD("OnHold"),

    CONFIRMED("Confirmed"),

    REJECTED("Rejected"),

    PAID("Paid"),

    INSUFFICIENTFUNDS("InsufficientFunds"),

    LIMITEXCEEDED("LimitExceeded"),

    USERAPPROVALFAILED("UserApprovalFailed"),

    USERAPPROVALTIMEOUT("UserApprovalTimeout"),

    USERAPPROVALCANCELLED("UserApprovalCancelled"),

    UNKNOWN("Unknown");

    private String value;

    PaymentStatusEnum(String value) {
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
    public static PaymentStatusEnum fromValue(String text) {
        for (PaymentStatusEnum b : PaymentStatusEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + text + "'");
    }
}
