package ir.adicom.mymoney

sealed interface TransactionEvent {
    data class AddTransaction(
        val title: String,
        val category: String,
        val amount: Double,
        val type: TransactionType
    ) : TransactionEvent

    data class DeleteTransaction(val transaction: Transaction) : TransactionEvent
}