package ir.adicom.mymoney.ui.transaction

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.adicom.mymoney.domain.model.Transaction
import ir.adicom.mymoney.domain.model.TransactionType


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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("My Money")
                }
            )
        },
        floatingActionButton = {
            ElevatedButton(
                onClick = {
                    onOpenAdd()
                }
            ) {
                Text("Add Transaction")
            }
        }
    ) {


        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Balance")
                Text("$ ${uiState.transactions.calculateBalance()}")
            }
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
                OperationState.Updating -> CircularProgressIndicator()
                is OperationState.Error -> Text(operation.message)
            }
        }
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
            .clickable {
                onDetailClick(transaction.id)
            }
    ) {
        Text(transaction.category)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(transaction.title)
            Text(transaction.formattedAmount, color = transaction.amountColor)
            IconButton(
                onClick = onClick,
                enabled = deleteEnabled
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

private val Transaction.amountColor: Color
    get() = when (type) {
        TransactionType.INCOME -> Color.Green
        TransactionType.EXPENSE -> Color.Red
    }

private val Transaction.formattedAmount: String
    get() = when (type) {
        TransactionType.INCOME -> "+ $${amount}"
        TransactionType.EXPENSE -> "- $${amount}"
    }