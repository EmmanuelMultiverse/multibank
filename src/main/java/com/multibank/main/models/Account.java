package com.multibank.main.models;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Table("accounts")
public class Account {
    @Id
    private final Long id;
    private final String accountNumber;
    private final BigDecimal balance;
}
