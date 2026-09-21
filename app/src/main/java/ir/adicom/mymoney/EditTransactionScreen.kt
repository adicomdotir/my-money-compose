package ir.adicom.mymoney

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionScreen(
    viewModel: EditTransactionViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
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
                TransactionEffect.TransactionUpdated -> {
                    onBack()
                }
                else -> Unit
            }
        }
    }

    LaunchedEffect(state.transaction) {
        state.transaction?.let { transaction ->
            titleTxtField = transaction.title
            categoryTxtField = transaction.category
            amountTxtField = transaction.amount.toString()
            selectedType = transaction.type
        }
    }

    if (state.isLoading) {
        CircularProgressIndicator()
    } else if (state.error != null) {
        Text("${state.error}")
    } else {

        Column() {
            Text("Edit Transaction")
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
                    val error = validateForm(
                        title = titleTxtField,
                        category = categoryTxtField,
                        amount = amountTxtField
                    )

                    formError = error
                    hasSubmitted = true

                    if (error.hasError()) {
                        return@ElevatedButton
                    }

                    viewModel.updateTransaction(
                        Transaction(
                            id = state.transaction!!.id,
                            title = titleTxtField,
                            category = categoryTxtField,
                            amount = amountTxtField.toDoubleOrNull() ?: 0.0,
                            type = selectedType,
                        )
                    )
                },
                enabled = operationState !is OperationState.Updating
            ) {
                Text("Add")
            }
        }
    }
}

