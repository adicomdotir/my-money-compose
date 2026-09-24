package ir.adicom.mymoney.ui.transaction

import ir.adicom.mymoney.domain.model.Transaction
import ir.adicom.mymoney.domain.model.TransactionType

sealed interface TransactionEvent {
    data class AddTransaction(
        val title: String,
        val category: String,
        val amount: Double,
        val type: TransactionType
    ) : TransactionEvent

    data class DeleteTransaction(val transaction: Transaction) : TransactionEvent
}