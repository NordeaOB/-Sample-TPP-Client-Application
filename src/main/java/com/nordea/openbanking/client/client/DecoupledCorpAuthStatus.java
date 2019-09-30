package com.nordea.openbanking.client.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecoupledCorpAuthStatus {
    private String status;
    private String code;

}
