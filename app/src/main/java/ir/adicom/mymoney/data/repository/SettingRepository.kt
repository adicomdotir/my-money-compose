package ir.adicom.mymoney.data.repository

import javax.inject.Inject

class SettingRepository() {
    private var currency: String = "Rial"

    fun saveCurrency(value: String) {
        currency = value
    }

    fun getCurrency() = currency
}