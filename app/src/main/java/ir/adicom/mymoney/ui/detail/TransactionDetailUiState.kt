package ir.adicom.mymoney.ui.detail

import ir.adicom.mymoney.domain.model.Transaction

data class TransactionDetailUiState(
    val transaction: Transaction? = null, val isLoading: Boolean = true, val error: String? = null
)