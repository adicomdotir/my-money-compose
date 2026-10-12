package ir.adicom.mymoney.ui.transaction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.adicom.mymoney.domain.model.Transaction
import ir.adicom.mymoney.domain.model.TransactionType
import ir.adicom.mymoney.ui.components.AppIconButton
import ir.adicom.mymoney.utils.DateUtils


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
                    Text("Transactions")
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onOpenAdd() },
                icon = {
                    Icon(Icons.Outlined.Add, contentDescription = null)
                },
                text = { Text("Add Transaction") }
            )
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
    onDetailClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable { onDetailClick(transaction.id) }
            .semantics(mergeDescendants = true) {},
        leadingContent = {
            CategoryAvatar(category = transaction.category)
        },
        headlineContent = {
            Text(
                text = transaction.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        supportingContent = {
            Text(
                text = transaction.category,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        trailingContent = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = transaction.formattedAmount,
                    style = MaterialTheme.typography.titleMedium,
                    color = transaction.amountColor,
                )
                Text(
                    text = DateUtils.convertTimestampToShamsi(transaction.timestamp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
    )

    // Delete action as a separate, discoverable affordance
    AppIconButton(
        icon = Icons.Outlined.Delete,
        contentDescription = "Delete ${transaction.title}",
        enabled = deleteEnabled,
        tint = MaterialTheme.colorScheme.error,
        onClick = onClick,
        modifier = Modifier.padding(start = 16.dp, end = 8.dp),
    )
}

@Composable
private fun CategoryAvatar(category: String) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier.size(40.dp),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = category.take(1).uppercase(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
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