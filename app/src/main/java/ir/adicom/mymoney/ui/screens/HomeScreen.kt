package ir.adicom.mymoney.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.ui.unit.dp
import com.example.expensetracker.ui.screens.CategoryScreen

/**
 * HomeScreen - مرکزی صفحہ جس میں navigation ہے
 */
@Composable
fun HomeScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> DashboardScreen()           // صفحہ اصلی
                1 -> CategoryScreen()            // دسته‌بندی
                2 -> ExpenseListScreen()         // هزینے
                3 -> ReportsScreen()             // رپورٹس
                4 -> SettingsScreen()            // ترتیبات
            }
        }
    }
}

/**
 * BottomNavigationBar - نیچے کی navigation
 */
@Composable
fun BottomNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            icon = { Icon(Icons.Default.Home, contentDescription = "صفحہ اصلی") },
            label = { Text("صفحہ اصلی") }
        )

        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            icon = { Icon(Icons.Default.List, contentDescription = "دسته‌بندی") },
            label = { Text("دسته‌بندی") }
        )

        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            icon = { Icon(Icons.Default.List, contentDescription = "هزینے") },
            label = { Text("هزینے") }
        )

        NavigationBarItem(
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) },
            icon = { Icon(Icons.Default.List, contentDescription = "رپورٹس") },
            label = { Text("رپورٹس") }
        )

        NavigationBarItem(
            selected = selectedTab == 4,
            onClick = { onTabSelected(4) },
            icon = { Icon(Icons.Default.Settings, contentDescription = "ترتیبات") },
            label = { Text("ترتیبات") }
        )
    }
}

/**
 * DashboardScreen - صفحہ اصلی (ابھی خالی ہے)
 */
@Composable
fun DashboardScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "صفحہ اصلی",
            style = MaterialTheme.typography.headlineMedium
        )
        Text("یہاں خلاصہ نمایا جائے گا")
    }
}

/**
 * ExpenseListScreen - هزینے کی لیست (ابھی خالی ہے)
 */
@Composable
fun ExpenseListScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "هزینے",
            style = MaterialTheme.typography.headlineMedium
        )
        Text("یہاں هزینے کی لیست ہوگی")
    }
}

/**
 * ReportsScreen - رپورٹس (ابھی خالی ہے)
 */
@Composable
fun ReportsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "رپورٹس",
            style = MaterialTheme.typography.headlineMedium
        )
        Text("یہاں رپورٹس ہوں گی")
    }
}

/**
 * SettingsScreen - ترتیبات (ابھی خالی ہے)
 */
@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "ترتیبات",
            style = MaterialTheme.typography.headlineMedium
        )
        Text("یہاں ترتیبات ہوں گی")
    }
}