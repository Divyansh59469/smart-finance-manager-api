package com.divyansh.smart_finance_manager_api.controller;

import com.divyansh.smart_finance_manager_api.model.Budget;
import com.divyansh.smart_finance_manager_api.model.User;
import com.divyansh.smart_finance_manager_api.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @GetMapping
    public List<Budget> getAll(@AuthenticationPrincipal User user) {
        return budgetService.getAllBudgets(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Budget add(
            @Valid @RequestBody Budget budget,
            @AuthenticationPrincipal User user
    ) {
        return budgetService.addBudget(budget, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        budgetService.deleteBudget(id, user);
    }
}