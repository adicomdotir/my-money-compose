package ir.adicom.mymoney.ui.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.adicom.mymoney.ui.components.CustomAppBar

@Composable
fun ReportScreen(
    viewModel: ReportViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val currency by viewModel.currency.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CustomAppBar(
                title = "Report",
                onBackClick = onBackClick
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
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        CustomChart(
                            data = listOf(0.4f, 0.7f, 0.5f, 0.9f, 0.3f, 0.6f, 0.8f),
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
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

/**
 * A custom bar chart layout.
 *
 * @param data A list of floats between 0.0 and 1.0 representing the relative height of each bar.
 */
@Composable
private fun CustomChart(
    data: List<Float>,
    modifier: Modifier = Modifier
) {
    val barColor = MaterialTheme.colorScheme.primary
    Layout(
        modifier = modifier,
        content = {
            data.forEach { _ ->
                Box(
                    modifier = Modifier.background(
                        color = barColor,
                        shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                    )
                )
            }
        }
    ) { measurables, constraints ->
        val spacing = 8.dp.roundToPx()
        val totalSpacing = spacing * (data.size - 1)
        val itemWidth = if (data.isNotEmpty()) {
            (constraints.maxWidth - totalSpacing) / data.size
        } else 0

        val placeables = measurables.mapIndexed { index, measurable ->
            val itemHeight = (constraints.maxHeight * data[index]).toInt()
            measurable.measure(Constraints.fixed(itemWidth, itemHeight))
        }

        layout(constraints.maxWidth, constraints.maxHeight) {
            var xPosition = 0
            placeables.forEach { placeable ->
                placeable.placeRelative(x = xPosition, y = constraints.maxHeight - placeable.height)
                xPosition += itemWidth + spacing
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
