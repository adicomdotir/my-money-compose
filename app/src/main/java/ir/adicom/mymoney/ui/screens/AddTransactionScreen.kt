package ir.adicom.mymoney.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.adicom.mymoney.ui.components.CustomAppBar
import ir.adicom.mymoney.ui.OperationState
import ir.adicom.mymoney.ui.TransactionEffect
import ir.adicom.mymoney.ui.TransactionEvent
import ir.adicom.mymoney.ui.TransactionType
import ir.adicom.mymoney.ui.viewmodel.TransactionViewModel

data class TransactionFormError(
    val title: String? = null,
    val category: String? = null,
    val amount: String? = null
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    viewModel: TransactionViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val operationState by viewModel.operationState.collectAsStateWithLifecycle()

    var titleTxtField by remember { mutableStateOf("") }
    var categoryTxtField by remember { mutableStateOf("") }
    var amountTxtField by remember { mutableStateOf("") }

    var formError by remember {
        mutableStateOf(TransactionFormError())
    }

    var hasSubmitted by remember { mutableStateOf(false) }

    val transactionTypes = TransactionType.entries
    var expanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf(TransactionType.EXPENSE) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { message ->
            when (message) {
                TransactionEffect.TransactionAdded -> {
                    onBack()
                }

                else -> Unit
            }
        }
    }

    Scaffold(
        topBar = {
            CustomAppBar("Add Transaction", onBackClick = onBack)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
        ) {
            when (val operation = operationState) {
                OperationState.Idle -> Unit
                OperationState.Adding -> CircularProgressIndicator()
                OperationState.Deleting -> CircularProgressIndicator()
                OperationState.Updating -> CircularProgressIndicator()
                is OperationState.Error -> Text(operation.message)
            }

            AppTextField(
                label = "Title",
                value = titleTxtField,
                onValueChange = {
                    titleTxtField = it

                    if (it.isNotBlank()) {
                        formError = formError.copy(title = null)
                    }
                },
                error = if (hasSubmitted) formError.title ?: "" else ""
            )
            AppTextField(
                label = "Category",
                value = categoryTxtField,
                onValueChange = {
                    categoryTxtField = it

                    if (it.isNotBlank()) {
                        formError = formError.copy(category = null)
                    }
                },
                error = if (hasSubmitted) formError.category ?: "" else ""
            )
            AppTextField(
                label = "Amount",
                value = amountTxtField,
                onValueChange = {
                    amountTxtField = it

                    formError = formError.copy(
                        amount = when {
                            amountTxtField.isBlank() -> "Fill amount"
                            amountTxtField.toDoubleOrNull() == null -> "Invalid amount"
                            amountTxtField.toDouble() <= 0 -> "Amount must be greater than 0"
                            else -> null
                        }
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                error = if (hasSubmitted) formError.amount ?: "" else ""
            )



            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {
                    TextField(
                        value = selectedType.name,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        transactionTypes.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item.name) },
                                onClick = {
                                    selectedType = item
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
            ElevatedButton(
                onClick = {
                    formError = validateForm(
                        title = titleTxtField,
                        category = categoryTxtField,
                        amount = amountTxtField
                    )

                    hasSubmitted = true

                    if (formError.hasError()) {
                        return@ElevatedButton
                    }

                    viewModel.onEvent(
                        TransactionEvent.AddTransaction(
                            title = titleTxtField,
                            category = categoryTxtField,
                            amount = amountTxtField.toDoubleOrNull() ?: 0.0,
                            type = selectedType,
                        )
                    )
                },
                enabled = operationState !is OperationState.Adding
            ) {
                Text("Add")
            }
        }
    }
}

@Composable
fun AppTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    error: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = keyboardOptions,
        isError = error.isNotEmpty(),
        supportingText = {
            if (error.isNotEmpty()) {
                Text(error)
            }
        }
    )
}

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

fun TransactionFormError.hasError(): Boolean =
    title != null || category != null || amount != null

