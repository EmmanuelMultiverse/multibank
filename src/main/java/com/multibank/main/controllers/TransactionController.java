package com.multibank.main.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multibank.main.models.TransactionRequest;
import com.multibank.main.services.TransactionService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/withdrawal")
    public ResponseEntity<String> withdrawalCash(@RequestBody TransactionRequest withdrawalRequest) {
        
        log.info("Initializing withdrawal from account: {}", withdrawalRequest.getAccountNumber());
        try {
            transactionService.withdrawalCash(withdrawalRequest.getAccountNumber(), withdrawalRequest.getAmount());
            log.info("Finished withdrawal!");
            return ResponseEntity.status(200).body(String.format("Withdrawal successful"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> depositCash(@RequestBody TransactionRequest depositRequest) {
        
        try {
            transactionService.depositCash(depositRequest.getAccountNumber(), depositRequest.getAmount());
            return ResponseEntity.ok().body("Deposit Successful");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    
}
