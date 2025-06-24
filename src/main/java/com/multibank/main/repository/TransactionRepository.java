package com.multibank.main.repository;

import org.springframework.data.repository.CrudRepository;

import com.multibank.main.models.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    
}
