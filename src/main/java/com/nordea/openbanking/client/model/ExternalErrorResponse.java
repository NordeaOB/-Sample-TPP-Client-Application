package com.nordea.openbanking.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExternalErrorResponse {

    @JsonProperty("_type")
    private TypeEnum type;

    @JsonProperty("group_header")
    private ExternalResponseHeader groupHeader = null;

    @JsonProperty("error")
    private ExternalError error = null;

    /**
     * Gets or Sets type
     */
    public enum TypeEnum {

        EXTERNALREQUESTECHO("ExternalRequestEcho"),

        EXTERNALERROR("ExternalError"),

        EXTERNALERRORRESPONSE("ExternalErrorResponse"),

        EXTERNALERRORRESPONSEV2("ExternalErrorResponseV2"),

        EXTERNALERRORRESPONSEV3("ExternalErrorResponseV3");

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

    @Data
    public class ExternalResponseHeader {

        @JsonProperty("creation_date_time")
        private LocalDateTime creationDateTime;

        @JsonProperty("http_code")
        private Integer httpCode;

        @JsonProperty("message_identification")
        private String messageIdentification;

        @JsonProperty("message_pagination")
        private String messagePagination = null;
    }

    @Data
    public class ExternalError {

        @JsonProperty("_type")
        private TypeEnum type;

        @JsonProperty("http_code")
        private Integer httpCode;

        @JsonProperty("request")
        private ExternalRequestEcho request = null;

        @JsonProperty("failures")
        private List<ExternalFailure> failures = null;

    }




}
