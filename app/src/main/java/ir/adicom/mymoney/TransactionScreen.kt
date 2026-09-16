package ir.adicom.mymoney

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TransactionScreen(modifier: Modifier = Modifier, viewModel: TransactionViewModel) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val operationState by viewModel.operationState.collectAsStateWithLifecycle()

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
        Text("Title:        Coffee")
        Text("Category:     Food")
        Text("Amount:       5")
        Text("Type:         Expense")
        ElevatedButton(
            onClick = {
                viewModel.onEvent(
                    TransactionEvent.AddTransaction(
                        title = "Coffee",
                        category = "Food",
                        amount = 5.0,
                        type = TransactionType.EXPENSE,
                    )
                )
            },
            enabled = operationState !is OperationState.Adding
        ) {
            Text("Add")
        }
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

fun addSignToAmount(transaction: Transaction): String {
    if (transaction.type == TransactionType.INCOME) {
        return "+ $${transaction.amount}"
    }
    return "- $${transaction.amount}"
}