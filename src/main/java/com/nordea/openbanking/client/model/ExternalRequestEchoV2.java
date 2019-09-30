package com.nordea.openbanking.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExternalRequestEchoV2 {

    @JsonProperty("_type")
    private ExternalErrorResponse.TypeEnum type;

    @JsonProperty("messageIdentifier")
    private String messageIdentifier;

    @JsonProperty("url")
    private String url;
}