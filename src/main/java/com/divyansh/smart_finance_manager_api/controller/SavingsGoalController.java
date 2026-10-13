package com.divyansh.smart_finance_manager_api.controller;

import com.divyansh.smart_finance_manager_api.dto.AddMoneyRequest;
import com.divyansh.smart_finance_manager_api.model.SavingsGoal;
import com.divyansh.smart_finance_manager_api.model.User;
import com.divyansh.smart_finance_manager_api.service.SavingsGoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class SavingsGoalController {

    private final SavingsGoalService savingsGoalService;

    @GetMapping
    public List<SavingsGoal> getAll(@AuthenticationPrincipal User user) {
        return savingsGoalService.getAllGoals(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SavingsGoal add(
            @Valid @RequestBody SavingsGoal goal,
            @AuthenticationPrincipal User user
    ) {
        return savingsGoalService.addGoal(goal, user);
    }

    @PostMapping("/{id}/add-money")
    public SavingsGoal addMoney(
            @PathVariable Long id,
            @Valid @RequestBody AddMoneyRequest request,
            @AuthenticationPrincipal User user
    ) {
        return savingsGoalService.addMoney(id, request.amount(), user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        savingsGoalService.deleteGoal(id, user);
    }
}
