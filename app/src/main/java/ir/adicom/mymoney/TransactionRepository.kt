package ir.adicom.mymoney

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

fun getFakeData(): List<Transaction> {
    return listOf(
        Transaction(
            id = 1,
            title = "Lunch",
            category = "Food",
            amount = 15.0,
            type = TransactionType.EXPENSE,
        ),
        Transaction(
            id = 1,
            title = "Uber",
            category = "Transport",
            amount = 22.0,
            type = TransactionType.EXPENSE,
        ),
        Transaction(
            id = 1,
            title = "Company",
            category = "Salary",
            amount = 3000.0,
            type = TransactionType.INCOME,
        ),
        Transaction(
            id = 1,
            title = "Amazon",
            category = "Shopping",
            amount = 80.0,
            type = TransactionType.EXPENSE,
        ),
    )
}

class TransactionRepository {
    fun getTransactions(): List<Transaction> = getFakeData()

    fun addTransaction(
        transaction: Transaction
    ) {

    }
}