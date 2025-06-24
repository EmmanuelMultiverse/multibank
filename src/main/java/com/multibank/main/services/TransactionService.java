package com.multibank.main.services;

import java.math.BigDecimal;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.multibank.main.models.Account;
import com.multibank.main.models.Transaction;
import com.multibank.main.repository.AccountRepository;
import com.multibank.main.repository.TransactionRepository;

@Service
public class TransactionService {
    Logger logger = Logger.getLogger("INFO");
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void withdrawalCash(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                            .orElseThrow(() -> new RuntimeException(String.format("Cannot find account with account number: %s", accountNumber)));
        
        BigDecimal newBalance = account.getBalance().subtract(amount);

        accountRepository.setBalanceByAccountNumber(newBalance, accountNumber);

        Account newAccount = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new RuntimeException());
        logger.info(newAccount.toString());

        transactionRepository.save(new Transaction(null, amount, "WITHDRAWAL", account.getId()));
    }

    @Transactional
    public void depositCash(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                            .orElseThrow(() -> new RuntimeException(String.format("Cannot find account with account number: %s", accountNumber)));
        
        BigDecimal newBalance = account.getBalance().add(amount);

        accountRepository.setBalanceByAccountNumber(newBalance, accountNumber);
        
        Account newAccount = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new RuntimeException());
        logger.info(newAccount.toString());

        transactionRepository.save(new Transaction(null, amount, "DEPOSIT", account.getId()));
    } 
}
