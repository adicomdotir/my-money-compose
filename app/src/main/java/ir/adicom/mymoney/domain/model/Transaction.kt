package ir.adicom.mymoney.domain.model

data class Transaction(
    val id: Long,
    val title: String,
    val category: String,
    val amount: Double,
    val type: TransactionType
)