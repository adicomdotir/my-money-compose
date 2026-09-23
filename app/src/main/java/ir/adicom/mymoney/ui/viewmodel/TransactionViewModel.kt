package ir.adicom.mymoney.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.adicom.mymoney.ui.OperationState
import ir.adicom.mymoney.domain.model.Transaction
import ir.adicom.mymoney.ui.TransactionEffect
import ir.adicom.mymoney.ui.TransactionEvent
import ir.adicom.mymoney.data.repository.TransactionRepository
import ir.adicom.mymoney.ui.TransactionType
import ir.adicom.mymoney.ui.TransactionUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
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

    private val _operationState = MutableStateFlow<OperationState>(OperationState.Idle)
    val operationState = _operationState.asStateFlow()

    private val _effect = MutableSharedFlow<TransactionEffect>()
    val effect = _effect.asSharedFlow()


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
            _operationState.value = OperationState.Deleting

            try {
                repository.deleteTransaction(transaction)
                _operationState.value = OperationState.Idle
            } catch (e: Exception) {
                _operationState.value =
                    OperationState.Error(e.message ?: "Unknown error")
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
            _operationState.value = OperationState.Adding
            try {
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
                _operationState.value = OperationState.Idle
                _effect.emit(TransactionEffect.TransactionAdded)
            } catch (e: Exception) {
                _operationState.value =
                    OperationState.Error(e.message ?: "Unknown error")
            }
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