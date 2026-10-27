package ir.adicom.mymoney.ui.transaction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.adicom.mymoney.domain.model.Transaction
import ir.adicom.mymoney.domain.model.TransactionType
import ir.adicom.mymoney.ui.components.AppIconButton
import ir.adicom.mymoney.ui.components.DeleteConfirmDialog
import ir.adicom.mymoney.utils.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    modifier: Modifier = Modifier,
    viewModel: TransactionViewModel = hiltViewModel(),
    onOpenAdd: () -> Unit,
    onDetailClick: (Long) -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val operationState by viewModel.operationState.collectAsStateWithLifecycle()

    var transactionToDelete by remember { mutableStateOf<Transaction?>(null) }

    // Memoize the balance calculation
    val balance = remember(uiState.transactions) {
        uiState.transactions.calculateBalance()
    }

    val isDeleting = operationState is OperationState.Deleting

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text("Transactions") })
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onOpenAdd,
                icon = { Icon(Icons.Outlined.Add, contentDescription = null) },
                text = { Text("Add Transaction") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            BalanceSection(balance = balance)

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Box(modifier = Modifier.weight(1f)) {
                when {
                    !uiState.error.isNullOrBlank() -> {
                        ErrorState(message = uiState.error!!)
                    }

                    uiState.isLoading -> {
                        LoadingState()
                    }

                    uiState.transactions.isEmpty() -> {
                        EmptyState(onAddClick = onOpenAdd)
                    }

                    else -> {
                        TransactionList(
                            transactions = uiState.transactions,
                            deleteEnabled = !isDeleting,
                            onDeleteClick = { transactionToDelete = it },
                            onItemClick = onDetailClick
                        )
                    }
                }

                // Global operation progress overlay
                (operationState as? OperationState.Adding)?.let {
                    OperationOverlay()
                }
                (operationState as? OperationState.Updating)?.let {
                    OperationOverlay()
                }
                (operationState as? OperationState.Deleting)?.let {
                    OperationOverlay()
                }
                (operationState as? OperationState.Error)?.let {
                    ErrorBanner(message = it.message)
                }
            }
        }
    }

    transactionToDelete?.let { transaction ->
        DeleteConfirmDialog(
            title = "Delete transaction?",
            message = "Are you sure you want to delete \"${transaction.title}\"? " +
                    "This action cannot be undone.",
            isLoading = isDeleting,
            onConfirm = {
                viewModel.onEvent(TransactionEvent.DeleteTransaction(transaction))
                transactionToDelete = null
            },
            onDismiss = { transactionToDelete = null }
        )
    }
}

@Composable
private fun BalanceSection(balance: Double, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Balance",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "$${balance}",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "This Month",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TransactionList(
    transactions: List<Transaction>,
    deleteEnabled: Boolean,
    onDeleteClick: (Transaction) -> Unit,
    onItemClick: (Long) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 8.dp,
            bottom = 96.dp // avoid FAB overlap
        ),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(
            items = transactions,
            key = { it.id }
        ) { transaction ->
            TransactionItem(
                transaction = transaction,
                deleteEnabled = deleteEnabled,
                onClick = { onItemClick(transaction.id) },
                onDeleteClick = { onDeleteClick(transaction) }
            )
        }
    }
}

@Composable
fun TransactionItem(
    transaction: Transaction,
    deleteEnabled: Boolean,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CategoryAvatar(category = transaction.category)

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = transaction.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = transaction.formattedAmount,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = transaction.amountColor
                )
                Text(
                    text = DateUtils.convertTimestampToShamsi(transaction.timestamp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            AppIconButton(
                icon = Icons.Outlined.Delete,
                contentDescription = "Delete ${transaction.title}",
                enabled = deleteEnabled,
                tint = MaterialTheme.colorScheme.error,
                onClick = onDeleteClick
            )
        }
    }
}

@Composable
private fun CategoryAvatar(category: String, modifier: Modifier = Modifier) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = modifier.size(40.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = category.take(1).uppercase(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyState(onAddClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.Settings,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "No transactions yet",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Your transactions will appear here",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = onAddClick) {
            Icon(Icons.Outlined.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Add Transaction")
        }
    }
}

@Composable
private fun ErrorState(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun OperationOverlay(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
            tonalElevation = 6.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                Text("Please wait…", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun ErrorBanner(message: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        color = MaterialTheme.colorScheme.errorContainer,
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}

/* -------------------- Extensions -------------------- */

private val Transaction.amountColor
    get() = when (type) {
        TransactionType.INCOME -> Color.Green
        TransactionType.EXPENSE -> Color.Red
    }

private val Transaction.formattedAmount: String
    get() = when (type) {
        TransactionType.INCOME -> "+ $${amount}"
        TransactionType.EXPENSE -> "- $${amount}"
    }