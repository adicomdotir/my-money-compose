package ir.adicom.mymoney

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TransactionViewModel(
    val repository: TransactionRepository
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

    fun getTransaction() {
        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            val result = repository.getTransactions()
            val balance =
                result.map { if (it.type == TransactionType.INCOME) it.amount else -it.amount }
                    .reduce { acc, trans -> acc + trans }
            _state.value =
                _state.value.copy(isLoading = false, transactions = result, balance = balance)

        }
    }
}