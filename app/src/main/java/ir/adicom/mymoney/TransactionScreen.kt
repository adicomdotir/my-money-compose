package ir.adicom.mymoney

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@ExperimentalMaterial3Api
@Composable
fun TransactionScreen(modifier: Modifier = Modifier, viewModel: TransactionViewModel) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val operationState by viewModel.operationState.collectAsStateWithLifecycle()

    var titleTxtField by remember { mutableStateOf("") }
    var categoryTxtField by remember { mutableStateOf("") }
    var amountTxtField by remember { mutableStateOf("") }

    var titleError by remember {
        mutableStateOf("")
    }
    var categoryError by remember {
        mutableStateOf("")
    }
    var amountError by remember {
        mutableStateOf("")
    }

    val transactionTypes = TransactionType.entries
    var expanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf(TransactionType.EXPENSE) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { message ->
            when (message) {
                TransactionEffect.TransactionAdded -> {
                    titleTxtField = ""
                    categoryTxtField = ""
                    amountTxtField = ""
                    selectedType = TransactionType.EXPENSE
                }
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text("My Money")
        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
        Text("Balance")
        Text("$ ${uiState.transactions.calculateBalance()}")
        Text("This Month")
        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)


        if (!uiState.error.isNullOrBlank()) {
            Text("${uiState.error}")
        } else

            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else

                if (uiState.transactions.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        items(uiState.transactions) {
                            TransactionItem(
                                transaction = it,
                                onClick = {
                                    viewModel.onEvent(TransactionEvent.DeleteTransaction(it))
                                },
                                deleteEnabled = operationState !is OperationState.Deleting
                            )
                        }
                    }
                } else {
                    Text("No Content")
                }

        when (val operation = operationState) {
            OperationState.Idle -> Unit
            OperationState.Adding -> CircularProgressIndicator()
            OperationState.Deleting -> CircularProgressIndicator()
            is OperationState.Error -> Text(operation.message)
        }

        Text("Add Transaction")
        AppTextField(
            label = "Title",
            value = titleTxtField,
            onValueChange = {
                titleTxtField = it
                titleError = ""
            },
            error = titleError
        )
        AppTextField(
            label = "Category",
            value = categoryTxtField,
            onValueChange = {
                categoryTxtField = it
                categoryError = ""
            },
            error = categoryError
        )
        AppTextField(
            label = "Amount",
            value = amountTxtField,
            onValueChange = {
                amountTxtField = it
                amountError = ""
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            error = amountError
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
                val amount = amountTxtField.toDoubleOrNull()

                var isValid = true

                if (titleTxtField.isBlank()) {
                    titleError = "Fill title"
                    isValid = false
                }

                if (categoryTxtField.isBlank()) {
                    categoryError = "Fill category"
                    isValid = false
                }

                when {
                    amount == null -> {
                        amountError = "Fill amount"
                        isValid = false
                    }

                    amount <= 0 -> {
                        amountError = "Invalid amount"
                        isValid = false
                    }
                }

                if (!isValid) {
                    return@ElevatedButton
                }

                viewModel.onEvent(
                    TransactionEvent.AddTransaction(
                        title = titleTxtField,
                        category = categoryTxtField,
                        amount = amount ?: 0.0,
                        type = selectedType,
                    )
                )
            },
            enabled = operationState !is OperationState.Adding
        ) {
            Text("Add")
        }

        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Composable
fun TransactionItem(transaction: Transaction, onClick: () -> Unit, deleteEnabled: Boolean) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(transaction.category)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(transaction.title)
            Text(addSignToAmount(transaction))
            IconButton(
                onClick = onClick,
                enabled = deleteEnabled
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
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

fun addSignToAmount(transaction: Transaction): String {
    if (transaction.type == TransactionType.INCOME) {
        return "+ $${transaction.amount}"
    }
    return "- $${transaction.amount}"
}