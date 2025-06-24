package com.multibank.main.models;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionRequest {
    private final String accountNumber;
    private final BigDecimal amount;
}
