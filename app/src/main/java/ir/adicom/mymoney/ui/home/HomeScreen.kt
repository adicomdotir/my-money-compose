package ir.adicom.mymoney.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.adicom.mymoney.ui.components.CustomAppBar

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val currency by viewModel.currency.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CustomAppBar(
                title = "Home",
                onBackClick = {}
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                Spacer(modifier = Modifier.height(8.dp))

                ReportSummary(
                    title = "Daily",
                    amount = "500 $currency"
                )
            }

            item {
                ReportSummary(
                    title = "Weekly",
                    amount = "1,500 $currency"
                )
            }

            item {
                ReportSummary(
                    title = "Monthly",
                    amount = "4,000 $currency"
                )
            }

            item {
                ReportSummary(
                    title = "Yearly",
                    amount = "60,000 $currency"
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Categories",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            items(10) {
                CategoryReportItem(
                    category = "Category Name $it",
                    amount = "${50 * it} $currency"
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun ReportSummary(
    title: String,
    amount: String
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = amount,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun CategoryReportItem(
    category: String,
    amount: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 8.dp,
                vertical = 12.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = amount,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}