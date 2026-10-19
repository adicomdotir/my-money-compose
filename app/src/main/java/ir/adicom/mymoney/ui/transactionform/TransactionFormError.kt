package ir.adicom.mymoney.ui.transactionform

data class TransactionFormError(
    val title: String? = null,
    val category: String? = null,
    val amount: String? = null
)

fun TransactionFormError.hasError(): Boolean =
    title != null || category != null || amount != null