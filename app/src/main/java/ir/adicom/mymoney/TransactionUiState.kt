package ir.adicom.mymoney

data class TransactionUiState(
    val transactions: List<Transaction>,
    val balance: Double,
    val isLoading: Boolean,
    val error: String?
)