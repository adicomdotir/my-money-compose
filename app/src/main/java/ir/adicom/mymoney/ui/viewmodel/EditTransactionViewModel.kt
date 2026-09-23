package ir.adicom.mymoney.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.adicom.mymoney.OperationState
import ir.adicom.mymoney.Transaction
import ir.adicom.mymoney.TransactionEffect
import ir.adicom.mymoney.TransactionRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditTransactionUiState(
    val transaction: Transaction? = null, val isLoading: Boolean = true, val error: String? = null
)

@HiltViewModel
class EditTransactionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, private val repository: TransactionRepository
) : ViewModel() {
    private val id: Long = checkNotNull(savedStateHandle["id"])

    private val _state =
        MutableStateFlow(EditTransactionUiState(isLoading = true))
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<TransactionEffect>()
    val effect = _effect.asSharedFlow()

    private val _operationState = MutableStateFlow<OperationState>(OperationState.Idle)
    val operationState = _operationState.asStateFlow()

    init {
        getTransactionById()
    }

    private fun getTransactionById() {
        viewModelScope.launch {
            try {
                val res = repository.getTransactionById(id)

                _state.value = _state.value.copy(
                    isLoading = false,
                    transaction = res
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            _operationState.value = OperationState.Updating

            try {
                repository.updateTransaction(transaction)
                _operationState.value = OperationState.Idle
                _effect.emit(TransactionEffect.TransactionUpdated)
            } catch (e: Exception) {
                _operationState.value = OperationState.Error(
                    e.message ?: "Unknown error"
                )
            }
        }
    }
}