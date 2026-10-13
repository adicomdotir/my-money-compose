package ir.adicom.mymoney.ui.transactionform

fun validateForm(
    title: String,
    category: String,
    amount: String
): TransactionFormError {
    val parsedAmount = amount.toDoubleOrNull()

    return TransactionFormError(
        title = if (title.isBlank()) "Fill title" else null,
        category = if (category.isBlank()) "Fill category" else null,
        amount = when {
            amount.isBlank() -> "Fill amount"
            parsedAmount == null -> "Invalid amount"
            parsedAmount <= 0 -> "Amount must be greater than 0"
            else -> null
        }
    )
}