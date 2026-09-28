package ir.adicom.mymoney.ui.report

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(): ViewModel() {
    private val _state = MutableStateFlow(
        ""
    )
    val state = _state.asStateFlow()

    init {
        sample()
    }

    fun sample() {
        viewModelScope.launch {
            val user = async {
                getUser()
            }
            val transaction = async {
                getTransaction()
            }

            val userResult = user.await()
            val transactionResult = transaction.await()

            Log.e("TAG", userResult)
            Log.e("TAG", transactionResult.toString())
        }
    }

    suspend fun getUser(): String {
        delay(5000)
        throw Exception("Error")
        return "Ali"
    }

    suspend fun getTransaction(): Int {
        delay(1000)
        return 500
    }

}