package ir.adicom.mymoney.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.adicom.mymoney.data.repository.SettingRepository
import ir.adicom.mymoney.data.repository.TransactionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    settingRepository: SettingRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    private val expenseType = "EXPENSE"

    val uiState: StateFlow<HomeUiState> = combine(
        transactionRepository.getTotalAmountSince(expenseType, getStartOfToday()),
        transactionRepository.getTotalAmountSince(expenseType, getStartOfWeek()),
        transactionRepository.getTotalAmountSince(expenseType, getStartOfMonth()),
        transactionRepository.getCategorySummariesSince(
            expenseType,
            getStartOfMonth()
        ),
    ) { daily, weekly, monthly, categories ->
        HomeUiState(
            dailyExpense = daily ?: 0.0,
            weeklyExpense = weekly ?: 0.0,
            monthlyExpense = monthly ?: 0.0,
            categorySummaries = categories,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )

    private fun getStartOfToday(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    private fun getStartOfWeek(): Long {
        return Calendar.getInstance().apply {
            firstDayOfWeek = Calendar.SATURDAY // تنظیم شروع هفته (مثلاً شنبه)
            set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    private fun getStartOfMonth(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    val currency: StateFlow<String> =
        settingRepository.getCurrency()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                "Rial"
            )
}