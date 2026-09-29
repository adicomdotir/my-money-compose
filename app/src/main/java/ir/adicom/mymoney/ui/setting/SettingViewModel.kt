package ir.adicom.mymoney.ui.setting

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.adicom.mymoney.data.repository.SettingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val repository: SettingRepository
): ViewModel() {

    private var _currency = MutableStateFlow("")
    val currency = _currency.asStateFlow()

    init {
        getCurrency()
    }

    fun getCurrency() {
        val res  = repository.getCurrency()
        _currency.value = res
    }

    fun saveCurrency(value: String) {
        repository.saveCurrency(value)
        getCurrency()

    }


}