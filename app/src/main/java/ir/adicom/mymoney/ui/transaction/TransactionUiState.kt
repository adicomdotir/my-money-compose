package ir.adicom.mymoney.ui.transaction

import ir.adicom.mymoney.domain.model.Transaction

data class TransactionUiState(
    val transactions: List<Transaction>,
    val isLoading: Boolean,
    val error: String?
)