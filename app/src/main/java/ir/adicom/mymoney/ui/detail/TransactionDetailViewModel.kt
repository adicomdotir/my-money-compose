package ir.adicom.mymoney.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.adicom.mymoney.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, private val repository: TransactionRepository
) : ViewModel() {
    private val id: Long = checkNotNull(savedStateHandle["id"])

    private val _state =
        MutableStateFlow(TransactionDetailUiState(isLoading = true))
    val state = _state.asStateFlow()

    fun getTransactionById() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val res = repository.getTransactionById(id)

                _state.update {
                    it.copy(
                        isLoading = false,
                        transaction = res
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error"
                    )
                }
            }
        }
    }
}