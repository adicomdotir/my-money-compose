package ir.adicom.mymoney.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingRepository {
    private val _currency: MutableStateFlow<String> = MutableStateFlow("Rial")

    fun saveCurrency(value: String) {
        _currency.value = value
    }

    fun getCurrency(): StateFlow<String> = _currency
}