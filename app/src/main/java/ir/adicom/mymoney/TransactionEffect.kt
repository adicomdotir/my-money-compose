package ir.adicom.mymoney

sealed interface TransactionEffect {
    data object TransactionAdded : TransactionEffect
    data object TransactionUpdated: TransactionEffect
}