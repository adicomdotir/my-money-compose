package ir.adicom.mymoney.ui.sample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SampleViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SampleUiState())
    val uiState: StateFlow<SampleUiState> = _uiState.asStateFlow()

    init {
        loadItems()
    }

    fun onIncrementClick() {
        _uiState.update { state ->
            state.copy(counter = state.counter + 1)
        }
    }

    fun onReloadClick() {
        loadItems()
    }

    /**
     * Pretends to fetch data from a repository so the screen can show
     * the loading / content / error states.
     */
    private fun loadItems() {
        viewModelScope.launch {
            _uiState.update { state ->
                state.copy(isLoading = true, error = null)
            }

            try {
                delay(800L)
                val items = (1..5).map { index -> "Sample item $index" }
                _uiState.update { state ->
                    state.copy(items = items, isLoading = false)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error"
                    )
                }
            }
        }
    }
}
