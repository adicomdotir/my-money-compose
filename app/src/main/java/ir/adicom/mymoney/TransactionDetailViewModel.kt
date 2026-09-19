package ir.adicom.mymoney

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: TransactionRepository
) : ViewModel() {
    private val id: Long =
        checkNotNull(savedStateHandle["id"])

    private val _transaction = MutableStateFlow<Transaction?>(null)
    val transaction = _transaction.asStateFlow()

    init {
        getTransactionById()
    }

    private fun getTransactionById() {
        viewModelScope.launch {
            _transaction.value = repository.getTransactionById(id)
        }
    }
}