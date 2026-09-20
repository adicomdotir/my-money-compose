package ir.adicom.mymoney

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TransactionDetailUiState(
    val transaction: Transaction? = null, val isLoading: Boolean = true, val error: String? = null
)

@HiltViewModel
class TransactionDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, private val repository: TransactionRepository
) : ViewModel() {
    private val id: Long = checkNotNull(savedStateHandle["id"])

    private val _state =
        MutableStateFlow<TransactionDetailUiState>(TransactionDetailUiState(isLoading = true))
    val state = _state.asStateFlow()

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
}