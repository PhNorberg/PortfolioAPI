package com.philiphiliphilip.myportfolioapi.transaction.repository;

import com.philiphiliphilip.myportfolioapi.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
