package ir.adicom.mymoney.ui.report

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class ReportViewModel(): ViewModel() {
    private val _state = MutableStateFlow(
        ""
    )
    val state = _state.asStateFlow()

}