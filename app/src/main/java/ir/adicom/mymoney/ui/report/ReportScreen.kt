package ir.adicom.mymoney.ui.report

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ir.adicom.mymoney.ui.components.CustomAppBar

@Composable
fun ReportScreen() {
    Scaffold(
        topBar = {
            CustomAppBar(
                title = "Report",
                onBackClick = {}
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Daily")
                Text("500$")
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Weekly")
                Text("1500$")
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Monthly")
                Text("4000$")
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Yearly")
                Text("60000$")
            }
        }
    }
}