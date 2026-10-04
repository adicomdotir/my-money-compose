package ir.adicom.mymoney.ui.home

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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    settingRepository: SettingRepository
): ViewModel() {
    private val _state = MutableStateFlow(
        ""
    )
    val state = _state.asStateFlow()

    val currency: StateFlow<String> =
        settingRepository.getCurrency()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                "Rial"
            )

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
        return "Ali"
    }

    suspend fun getTransaction(): Int {
        delay(1000)
        return 500
    }

}