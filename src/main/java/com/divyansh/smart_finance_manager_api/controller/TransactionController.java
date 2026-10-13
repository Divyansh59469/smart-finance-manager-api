package com.divyansh.smart_finance_manager_api.controller;

import com.divyansh.smart_finance_manager_api.dto.TransactionSummary;
import com.divyansh.smart_finance_manager_api.model.Transaction;
import com.divyansh.smart_finance_manager_api.model.User;
import com.divyansh.smart_finance_manager_api.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public List<Transaction> getAll(@AuthenticationPrincipal User user) {
        return transactionService.getAllTransactions(user);
    }

    @GetMapping("/summary")
    public TransactionSummary getSummary(@AuthenticationPrincipal User user) {
        return transactionService.getSummary(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction add(
            @Valid @RequestBody Transaction transaction,
            @AuthenticationPrincipal User user
    ) {
        return transactionService.addTransaction(transaction, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        transactionService.deleteTransaction(id, user);
    }
}