package ir.adicom.mymoney.ui.sample

data class SampleUiState(
    val counter: Int = 0,
    val items: List<String> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
