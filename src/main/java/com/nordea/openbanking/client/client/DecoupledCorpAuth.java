package com.nordea.openbanking.client.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecoupledCorpAuth {
    private String accessId;
    private String clientToken;
    private String status;
}
