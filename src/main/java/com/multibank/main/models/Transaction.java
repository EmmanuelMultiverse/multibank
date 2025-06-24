package com.multibank.main.models;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Table("transactions")
public class Transaction {
    @Id
    private final Long id;
    private final BigDecimal amount;
    private final String type;
    private final Long accountId;
}
