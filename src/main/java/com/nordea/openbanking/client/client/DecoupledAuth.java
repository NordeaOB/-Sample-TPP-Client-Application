package com.nordea.openbanking.client.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecoupledAuth {
    private String orderRef;
    private String tppToken;
}
