package ir.adicom.mymoney

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface TransactionEvent {
    data object LoadTransactions : TransactionEvent
    data class AddTransaction(
        val title: String,
        val category: String,
        val amount: Double,
        val type: TransactionType
    ) : TransactionEvent
}

class TransactionViewModel(
    private val repository: TransactionRepository
) : ViewModel() {
    private val _state = MutableStateFlow(
        TransactionUiState(
            transactions = listOf(),
            balance = 0.0,
            isLoading = false,
            error = null
        )
    )
    val state: StateFlow<TransactionUiState> = _state.asStateFlow()

    fun onEvent(event: TransactionEvent) {
        when (event) {
            is TransactionEvent.LoadTransactions -> getTransaction()
            is TransactionEvent.AddTransaction -> {
                addTransaction(
                    title = event.title,
                    category = event.category,
                    amount = event.amount,
                    type = event.type
                )
            }
        }
    }

    private fun getTransaction() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val result = repository.getTransactions()
            val balance =
                result.sumOf { if (it.type == TransactionType.INCOME) it.amount else -it.amount }
            _state.value =
                _state.value.copy(isLoading = false, transactions = result, balance = balance)

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
                id = System.currentTimeMillis(),
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