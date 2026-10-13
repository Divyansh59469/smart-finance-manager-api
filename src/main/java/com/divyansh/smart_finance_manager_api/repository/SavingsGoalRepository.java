package com.divyansh.smart_finance_manager_api.repository;

import com.divyansh.smart_finance_manager_api.model.SavingsGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, Long> {

    List<SavingsGoal> findByUserIdOrderByIdAsc(Long userId);

    Optional<SavingsGoal> findByIdAndUserId(Long id, Long userId);
}
