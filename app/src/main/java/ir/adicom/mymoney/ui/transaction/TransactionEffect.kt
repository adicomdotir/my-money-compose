package ir.adicom.mymoney.ui.transaction

sealed interface TransactionEffect {
    data object TransactionAdded : TransactionEffect
    data object TransactionUpdated: TransactionEffect
}