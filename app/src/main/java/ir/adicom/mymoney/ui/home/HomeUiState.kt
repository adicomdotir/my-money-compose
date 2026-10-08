package ir.adicom.mymoney.ui.home

import ir.adicom.mymoney.domain.model.CategorySummary

data class HomeUiState(
    val dailyExpense: Double = 0.0,
    val weeklyExpense: Double = 0.0,
    val monthlyExpense: Double = 0.0,
    val categorySummaries: List<CategorySummary> = emptyList(),
    val isLoading: Boolean = true
)