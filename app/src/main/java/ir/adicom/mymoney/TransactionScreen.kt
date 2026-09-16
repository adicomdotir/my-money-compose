package ir.adicom.mymoney

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TransactionScreen(modifier: Modifier = Modifier, viewModel: TransactionViewModel) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.getTransaction()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text("My Money")
        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
        Text("Balance")
        Text("$ ${uiState.balance}")
        Text("This Month")
        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(uiState.transactions) {
                TransactionItem(it)
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(transaction.category)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(transaction.title)
            Text(addSignToAmount(transaction))
        }
    }
}

fun addSignToAmount(transaction: Transaction): String {
    if (transaction.type == TransactionType.INCOME) {
        return "+ $${transaction.amount}"
    }
    return  "- $${transaction.amount}"
}