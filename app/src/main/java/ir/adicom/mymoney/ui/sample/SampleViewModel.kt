package ir.adicom.mymoney.ui.sample

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
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

        supervisorScopeSpl()

        viewModelScope.launch {
            flow {
                emit(1)
                delay(1000)
                emit(2)
                delay(1000)
                emit(3)
            }.flatMapConcat {
                flow {
                    delay(200)
                    emit(it)
                }
            }.collect {
                println(it)
            }

            flowOf('a', 'b', 'c').collectLatest { value ->
                delay(500L)
                println("$value")
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        Log.e("TAG", "OnCleared $this")
    }

    fun flowSample() {
        viewModelScope.launch {
            flow {
                println("Start")
                emit(1)
                println("after 1")
                throw RuntimeException("Error")
            }.retryWhen { _, attempt ->
                println("Retry attempt: $attempt")
                attempt < 2
            }.catch {
                println("Error: $it")
                emit(-1)
            }.collect {
                println("Value: $it")
            }
        }
    }

    fun flowSample2() {

        viewModelScope.launch {
            val state = flow {
                delay(500)
                emit(1)
                delay(500)
                emit(2)
                delay(500)
                throw RuntimeException("Error")
            }
                .retryWhen { _, attempt ->
                    attempt < 1
                }
                .catch {
                    emit(-1)
                }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = 0
                )

            state.collect {
                println("value $it")
            }
        }
    }

    fun flowSample3() {
        val state = MutableStateFlow(0)
        val events = MutableSharedFlow<Int>(replay = 0)



        viewModelScope.launch {
            state.collect {
                println("State: $it")
            }
        }

        viewModelScope.launch {
            events.collect {
                println("Event: $it")
            }
        }

        state.value = 5
        viewModelScope.launch {
            events.emit(5)
        }
    }

    fun withContextSpl() {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    println("Before delay")
                    delay(500L)
                    println("after delay")
                    this.cancel()
                    println("after cancel")
                }

                println("Result: $result")
            } catch (e: CancellationException) {
                println("${e.message}")
            }
        }
    }

    fun coroutineScopeSpl() {
        viewModelScope.launch {
            try {
                coroutineScope {
                    launch {
                        throw RuntimeException("Error")
                    }

                    launch {
                        delay(1_000)
                        println("Second task")
                    }
                }
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    fun supervisorScopeSpl() {
        viewModelScope.launch {
            supervisorScope {
                val first = async {
                    delay(100)
                    throw Exception("alakjsdjflsad")
                }

                val second = async {
                    delay(5000)
                    "Success"
                }


                try {
                    val results = awaitAll(first, second)
                    println(results)
                } catch (e: Exception) {
                    // Handle the exception here (e.g., log it or show an error)
                    println("One of the tasks failed: ${e.message}")
                }
            }
        }
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
                        isLoading = false, error = e.message ?: "Unknown error"
                    )
                }
            }
        }
    }
}
