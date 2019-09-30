package com.nordea.openbanking.client.model.accounts;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TransactionFilterCriteria {
    private LocalDate fromDate;
    private LocalDate toDate;
}
