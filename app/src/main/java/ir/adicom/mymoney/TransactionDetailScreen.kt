package ir.adicom.mymoney

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TransactionDetailScreen(
    viewModel: TransactionDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize(),
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
            }
        }
    }
}