package com.divyansh.smart_finance_manager_api.service;

import com.divyansh.smart_finance_manager_api.model.SavingsGoal;
import com.divyansh.smart_finance_manager_api.model.User;
import com.divyansh.smart_finance_manager_api.repository.SavingsGoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SavingsGoalService {

    private final SavingsGoalRepository savingsGoalRepository;

    public List<SavingsGoal> getAllGoals(User user) {
        return savingsGoalRepository.findByUserIdOrderByIdAsc(user.getId());
    }

    public SavingsGoal addGoal(SavingsGoal goal, User user) {
        goal.setId(null);
        goal.setSaved(BigDecimal.ZERO);
        goal.setUser(user);
        return savingsGoalRepository.save(goal);
    }

    @Transactional
    public SavingsGoal addMoney(Long id, BigDecimal amount, User user) {
        SavingsGoal goal = findOwnedGoal(id, user);

        BigDecimal newSaved = goal.getSaved().add(amount);

        if (newSaved.compareTo(goal.getTarget()) > 0) {
            BigDecimal canAdd = goal.getTarget().subtract(goal.getSaved());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You can only add " + canAdd + " more to this goal");
        }

        goal.setSaved(newSaved);
        return savingsGoalRepository.save(goal);
    }

    public void deleteGoal(Long id, User user) {
        savingsGoalRepository.delete(findOwnedGoal(id, user));
    }

    private SavingsGoal findOwnedGoal(Long id, User user) {
        return savingsGoalRepository
                .findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Savings goal not found"));
    }
}
