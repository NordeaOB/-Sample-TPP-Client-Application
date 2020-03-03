package com.nordea.openbanking.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExternalRequestEcho {

    @JsonProperty("url")
    private String url;
}