package ir.adicom.mymoney

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailScreen(
    viewModel: TransactionDetailViewModel = hiltViewModel(),
    onEdit: (Long) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getTransactionById()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Transaction Detail")
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            when {
                state.isLoading -> CircularProgressIndicator()

                state.error != null -> Text(state.error ?: "")

                state.transaction != null -> {
                    Text("ID = ${state.transaction!!.id}")
                    Text("title = ${state.transaction!!.title}")
                    Text("category = ${state.transaction!!.category}")
                    Text("amount = ${state.transaction!!.amount}")
                    ElevatedButton(
                        onClick = {
                            onEdit(state.transaction!!.id)
                        }
                    ) {
                        Text("Edit")
                    }
                }
            }
        }
    }
}