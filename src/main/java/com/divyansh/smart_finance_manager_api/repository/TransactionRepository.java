package com.divyansh.smart_finance_manager_api.repository;

import com.divyansh.smart_finance_manager_api.model.Transaction;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserId(Long userId);

    List<Transaction> findByUserId(Long userId, Sort sort);

    Optional<Transaction> findByIdAndUserId(Long id, Long userId);
}