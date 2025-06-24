package com.multibank.main.configs;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.multibank.main.models.Account;
import com.multibank.main.models.Transaction;
import com.multibank.main.repository.AccountRepository;
import com.multibank.main.repository.TransactionRepository;

@Configuration
public class DataLoader {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository; // Assuming this repository exists

    /**
     * Constructor for Dependency Injection.
     * Spring will automatically inject instances of AccountRepository and TransactionRepository.
     * @param accountRepository The repository for Account entities.
     * @param transactionRepository The repository for Transaction entities.
     */
    public DataLoader(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * This method will be executed once the application context is fully loaded.
     * It's used here to insert initial data into the H2 database.
     *
     * @param args Command line arguments (not used here).
     * @return A CommandLineRunner bean.
     */
    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
        // Check if accounts already exist to prevent duplicate inserts on restart
            System.out.println("Loading initial H2 data...");

            // Create and save Account entities
            Account account1 = new Account(null, "ACC001", new BigDecimal("1000.00"));
            Account account2 = new Account(null, "ACC002", new BigDecimal("500.50"));

            account1 = accountRepository.save(account1); // Save to get the generated ID
            account2 = accountRepository.save(account2); // Save to get the generated ID

            // Create and save Transaction entities, linking them to accounts
            // Note: The 'id' parameter for Transaction is 'null' as it's auto-generated.
            // The 'accountId' parameter links the transaction to a specific account.
            transactionRepository.save(new Transaction(null, new BigDecimal("100.00"), "DEPOSIT", account1.getId()));
            transactionRepository.save(new Transaction(null, new BigDecimal("50.00"), "WITHDRAWAL", account1.getId()));
            transactionRepository.save(new Transaction(null, new BigDecimal("200.00"), "DEPOSIT", account2.getId()));

            System.out.println("H2 data loaded successfully.");
            System.out.println("H2 database already contains data. Skipping initial load.");
        };
    }
}
