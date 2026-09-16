package ir.adicom.mymoney

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val repository: TransactionRepository
) : ViewModel() {
    private val _state = MutableStateFlow(
        TransactionUiState(
            transactions = listOf(),
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
            try {
                _state.value = _state.value.copy(isLoading = true)
                val result= repository.getTransactions().stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = emptyList()
                )
            } catch (e: Exception) {
                _state.value =
                    _state.value.copy(isLoading = false, error = e.message)

            }
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