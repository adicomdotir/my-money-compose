package ir.adicom.mymoney

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val repository: TransactionRepository
) : ViewModel() {

    val state = repository.getTransactions()
        .map {
            TransactionUiState(
                transactions = it,
                isLoading = false,
                error = null
            )
        }.catch {
            emit(
                TransactionUiState(
                    transactions = emptyList(),
                    isLoading = false,
                    error = it.message
                )
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TransactionUiState(
                transactions = emptyList(),
                isLoading = true,
                error = null
            )
        )

    fun onEvent(event: TransactionEvent) {
        when (event) {
            is TransactionEvent.AddTransaction -> {
                addTransaction(
                    title = event.title,
                    category = event.category,
                    amount = event.amount,
                    type = event.type
                )
            }

            is TransactionEvent.DeleteTransaction -> {
                deleteTransaction(event.transaction)
            }
        }
    }

    private fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }

    private fun addTransaction(
        title: String,
        category: String,
        amount: Double,
        type: TransactionType
    ) {
        viewModelScope.launch {
            val transaction = Transaction(
                id = 0L,
                title = title,
                category = category,
                amount = amount,
                type = type
            )
            repository.addTransaction(
                transaction
            )
        }
    }
}

fun List<Transaction>.calculateBalance(): Double =
    sumOf {
        if (it.type == TransactionType.INCOME) {
            it.amount
        } else {
            -it.amount
        }
    }