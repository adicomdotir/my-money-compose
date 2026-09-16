package ir.adicom.mymoney

data class TransactionUiState(
    val transactions: List<Transaction>,
    val isLoading: Boolean,
    val error: String?
)