package com.multibank.main.services;

import java.math.BigDecimal;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.multibank.main.models.Account;
import com.multibank.main.models.Transaction;
import com.multibank.main.repository.AccountRepository;
import com.multibank.main.repository.TransactionRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        log.info("Getting account info..");
        Account account = accountRepository.findByAccountNumber(accountNumber)
                            .orElseThrow(() -> new RuntimeException(String.format("Cannot find account with account number: %s", accountNumber)));
        log.info("Succesfuly got account info!");

        log.info("Processing withdrawal of amount {}", amount);
        BigDecimal newBalance = account.getBalance().subtract(amount);

        accountRepository.setBalanceByAccountNumber(newBalance, accountNumber);
        
        log.info("Succesfully completed withdrawal!");
        Account newAccount = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new RuntimeException());
        logger.info(newAccount.toString());

        log.info("Storing new transcation..");
        transactionRepository.save(new Transaction(null, amount, "WITHDRAWAL", account.getId()));
        log.info("Succesfuly stored new transaction!");
    }

    @Transactional
    public void depositCash(String accountNumber, BigDecimal amount) {
        log.info("Getting account info..");
        Account account = accountRepository.findByAccountNumber(accountNumber)
                            .orElseThrow(() -> new RuntimeException(String.format("Cannot find account with account number: %s", accountNumber)));
        log.info("Succesfuly got account info!");

        log.info("Processing deposit..");
        BigDecimal newBalance = account.getBalance().add(amount);

        accountRepository.setBalanceByAccountNumber(newBalance, accountNumber);
        log.info("Successfuly completed deposit!");
        
        log.info("Storing new transaction..");
        transactionRepository.save(new Transaction(null, amount, "DEPOSIT", account.getId()));
        log.info("Succesfuly stored new transaction!");
    } 
}
