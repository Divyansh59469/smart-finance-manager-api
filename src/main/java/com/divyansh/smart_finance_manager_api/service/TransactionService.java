package com.divyansh.smart_finance_manager_api.service;

import com.divyansh.smart_finance_manager_api.dto.TransactionSummary;
import com.divyansh.smart_finance_manager_api.model.Transaction;
import com.divyansh.smart_finance_manager_api.model.TransactionType;
import com.divyansh.smart_finance_manager_api.model.User;
import com.divyansh.smart_finance_manager_api.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public Transaction addTransaction(Transaction transaction, User user) {
        transaction.setId(null);
        transaction.setUser(user);
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions(User user) {
        return transactionRepository.findByUserId(
                user.getId(),
                Sort.by(Sort.Direction.DESC, "date")
        );
    }

    public void deleteTransaction(Long id, User user) {
        Transaction transaction = transactionRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Transaction not found"));

        transactionRepository.delete(transaction);
    }

    public TransactionSummary getSummary(User user) {
        List<Transaction> all = transactionRepository.findByUserId(user.getId());

        BigDecimal income = sumByType(all, TransactionType.INCOME);
        BigDecimal expenses = sumByType(all, TransactionType.EXPENSE);

        return new TransactionSummary(income, expenses, income.subtract(expenses));
    }

    private BigDecimal sumByType(List<Transaction> transactions, TransactionType type) {
        return transactions.stream()
                .filter(t -> t.getType() == type)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}