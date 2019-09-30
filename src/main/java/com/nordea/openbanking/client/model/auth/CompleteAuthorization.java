package com.nordea.openbanking.client.model.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompleteAuthorization {
    @NotNull
    @Size(min = 2, max = 3)
    private String country;
    @Size(min = 6, max = 14)
    private String netbankID;
    @Min(10)
    @Max(129600)
    private int duration;
    private String firstAuthorizer;
    @NotNull
    private List<String> scopes = new ArrayList<>();
    @NotNull
    private List<String> accountNumbers = new ArrayList<>();
}
