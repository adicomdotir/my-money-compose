package ir.adicom.mymoney.ui.report

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.adicom.mymoney.data.repository.SettingRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    repository: SettingRepository
): ViewModel() {
    private val _state = MutableStateFlow(
        ""
    )
    val state = _state.asStateFlow()

    val currency: StateFlow<String> =
        repository.getCurrency()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                "Rial"
            )

    init {
        Log.e("TAG", "Init")
        sample()
    }

    override fun onCleared() {
        Log.e("TAG", "OnCleared")
        super.onCleared()
    }

    fun sample() {

        viewModelScope.launch {

            sample2()

            log("1")
            val user = async {
                getUser()
            }

            log("2")
            val transaction = async {
                getTransaction()
            }

            log("3")

            val userResult = user.await()

            log("4")
            val transactionResult = transaction.await()

            Log.wtf("TAG", userResult)
            Log.wtf("TAG", transactionResult.toString())
        }
    }

    suspend fun sample2() {
        flow {
            emit("one")
            emit("two")
            throw Exception("it's error")
        }.retryWhen { cause, attempt ->
            delay(2000L)
            true
        }.catch {
            emit("error")
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            "empty"
        ).collect {
            log(it)
        }
    }

    fun log(msg: String) {
        Log.wtf("TAG", msg)
    }

    suspend fun getUser(): String {
        delay(5000)
        return "Ali"
    }

    suspend fun getTransaction(): Int {
        delay(1000)
        return 500
    }

}