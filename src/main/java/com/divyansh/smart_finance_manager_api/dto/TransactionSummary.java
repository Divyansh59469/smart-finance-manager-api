package com.divyansh.smart_finance_manager_api.dto;

import java.math.BigDecimal;

public record TransactionSummary(
        BigDecimal totalIncome,
        BigDecimal totalExpenses,
        BigDecimal balance
) {
}