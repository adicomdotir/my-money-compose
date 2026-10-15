package ir.adicom.mymoney.ui.sample

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SampleViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SampleUiState())
    val uiState: StateFlow<SampleUiState> = _uiState.asStateFlow()


    init {
        Log.e("TAG", "Init $this")
        loadItems()
        viewModelScope.launch {
            val flow = flowOf(1, 2).onEach { delay(10) }
            val flow2 = flowOf("a", "b", "c").onEach { delay(15) }
            combine(flow, flow2) { i, s -> i.toString() + s }.collect {
                println(it) // Will print "1a 2a 2b 2c"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.e("TAG", "OnCleared $this")
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
