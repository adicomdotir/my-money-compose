package ir.adicom.mymoney.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.adicom.mymoney.data.repository.SettingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val repository: SettingRepository
) : ViewModel() {

    val currency: StateFlow<String> =
        repository.getCurrency()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                "Rial"
            )

    fun saveCurrency(value: String) {
        repository.saveCurrency(value)
    }
}