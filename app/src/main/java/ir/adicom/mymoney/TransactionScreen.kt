package ir.adicom.mymoney

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@ExperimentalMaterial3Api
@Composable
fun TransactionScreen(
    modifier: Modifier = Modifier,
    viewModel: TransactionViewModel = hiltViewModel(),
    onOpenAdd: () -> Unit,
    onDetailClick: (Long) -> Unit,
) {
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
                        items(uiState.transactions) { it ->
                            TransactionItem(
                                transaction = it,
                                onClick = {
                                    viewModel.onEvent(TransactionEvent.DeleteTransaction(it))
                                },
                                onDetailClick = { id ->
                                    onDetailClick(id)
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



        ElevatedButton(onClick = {
            onOpenAdd()
        }) {
            Text("Go to add transaction screen")
        }

        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Composable
fun TransactionItem(
    transaction: Transaction,
    onClick: () -> Unit,
    deleteEnabled: Boolean,
    onDetailClick: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                onDetailClick(transaction.id)
            }
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