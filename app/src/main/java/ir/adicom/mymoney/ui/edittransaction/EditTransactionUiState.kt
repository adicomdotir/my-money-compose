package ir.adicom.mymoney.ui.edittransaction

import ir.adicom.mymoney.domain.model.Transaction

data class EditTransactionUiState(
    val transaction: Transaction? = null, val isLoading: Boolean = true, val error: String? = null
)