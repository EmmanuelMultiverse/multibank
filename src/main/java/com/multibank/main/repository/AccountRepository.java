package com.multibank.main.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.multibank.main.models.Account;

public interface AccountRepository extends CrudRepository<Account, Long> {

    @Modifying
    @Query("UPDATE \"accounts\" SET balance = :newBalance WHERE account_number = :accountNumber")
    int setBalanceByAccountNumber(@Param("newBalance") BigDecimal newBalance, @Param("accountNumber") String accountNumber);

    Optional<Account> findByAccountNumber(String accountNumber);
}
