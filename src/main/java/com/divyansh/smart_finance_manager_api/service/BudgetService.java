package com.divyansh.smart_finance_manager_api.service;

import com.divyansh.smart_finance_manager_api.model.Budget;
import com.divyansh.smart_finance_manager_api.model.User;
import com.divyansh.smart_finance_manager_api.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;

    public List<Budget> getAllBudgets(User user) {
        return budgetRepository.findByUserIdOrderByIdAsc(user.getId());
    }

    public Budget addBudget(Budget budget, User user) {
        if (budgetRepository.existsByUserIdAndCategory(user.getId(), budget.getCategory())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "A budget for this category already exists");
        }

        budget.setId(null);
        budget.setUser(user);
        return budgetRepository.save(budget);
    }

    public void deleteBudget(Long id, User user) {
        Budget budget = budgetRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Budget not found"));

        budgetRepository.delete(budget);
    }
}