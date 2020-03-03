package com.nordea.openbanking.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ExternalErrorResponse {

    @JsonProperty("group_header")
    private ExternalResponseHeader groupHeader = null;

    @JsonProperty("error")
    private ExternalError error = null;

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

        @JsonProperty("request")
        private ExternalRequestEcho request = null;

        @JsonProperty("failures")
        private List<ExternalFailure> failures = null;

    }




}
