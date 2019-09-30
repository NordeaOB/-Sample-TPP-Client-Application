package com.nordea.openbanking.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExternalRequestEcho {

    @JsonProperty("_type")
    private ExternalErrorResponse.TypeEnum type;

    @JsonProperty("message_identifier")
    private String messageIdentifier;

    @JsonProperty("url")
    private String url;
}