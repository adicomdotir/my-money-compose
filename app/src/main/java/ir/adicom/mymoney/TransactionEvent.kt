package ir.adicom.mymoney

sealed interface TransactionEvent {
    data object LoadTransactions : TransactionEvent
    data class AddTransaction(
        val title: String,
        val category: String,
        val amount: Double,
        val type: TransactionType
    ) : TransactionEvent
}