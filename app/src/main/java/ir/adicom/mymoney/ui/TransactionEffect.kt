package ir.adicom.mymoney.ui

sealed interface TransactionEffect {
    data object TransactionAdded : TransactionEffect
    data object TransactionUpdated: TransactionEffect
}