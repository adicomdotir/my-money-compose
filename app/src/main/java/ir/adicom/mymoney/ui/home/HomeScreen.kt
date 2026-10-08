package ir.adicom.mymoney.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.adicom.mymoney.R
import ir.adicom.mymoney.ui.components.CustomAppBar
import kotlinx.coroutines.launch

enum class DrawerItem {
    Report,
    Setting,
    Transaction
}

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onDrawerClick: (DrawerItem) -> Unit,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val currency by viewModel.currency.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet() {
                Spacer(modifier = Modifier.height(26.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "",
                    modifier = Modifier
                        .size(150.dp)
                        .fillMaxWidth()
                        .align(CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(26.dp))
                Text(
                    "Report",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable(onClick = {
                            onDrawerClick(DrawerItem.Report)
                        })
                )
                Text(
                    "Setting",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable(onClick = {
                            onDrawerClick(DrawerItem.Setting)
                        })
                )
                Text(
                    "Transaction",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable(onClick = {
                            onDrawerClick(DrawerItem.Transaction)
                        })
                )
            }
        }
    ) {


        Scaffold(
            topBar = {
                CustomAppBar(
                    title = "Home",
                    onBackClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            },
        ) { paddingValues ->

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
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
                            amount = "${uiState.dailyExpense} $currency"
                        )
                    }

                    item {
                        ReportSummary(
                            title = "Weekly",
                            amount = "${uiState.weeklyExpense} $currency"
                        )
                    }

                    item {
                        ReportSummary(
                            title = "Monthly",
                            amount = "${uiState.monthlyExpense} $currency"
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Categories",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    items(uiState.categorySummaries) {
                        CategoryReportItem(
                            category = "${it.category}",
                            amount = "${it.totalAmount} $currency"
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
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