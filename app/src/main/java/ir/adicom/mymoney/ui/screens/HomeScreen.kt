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

/**
 * HomeScreen - صفحه خانه
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
                0 -> DashboardScreen()      // صفحه خانه
                1 -> CategoryScreen()       // دسته‌بندی‌ها
                2 -> ExpenseListScreen()    // هزینه‌ها
                3 -> ReportsScreen()        // گزارش‌ها
                4 -> SettingsScreen()       // تنظیمات
            }
        }
    }
}

/**
 * BottomNavigationBar - نوار ناوبری پایین
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
            icon = { Icon(Icons.Default.Home, contentDescription = "خانه") },
            label = { Text("خانه") }
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
            icon = { Icon(Icons.Default.List, contentDescription = "هزینه‌ها") },
            label = { Text("هزینه‌ها") }
        )

        NavigationBarItem(
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) },
            icon = { Icon(Icons.Default.List, contentDescription = "گزارش‌ها") },
            label = { Text("گزارش‌ها") }
        )

        NavigationBarItem(
            selected = selectedTab == 4,
            onClick = { onTabSelected(4) },
            icon = { Icon(Icons.Default.Settings, contentDescription = "تنظیمات") },
            label = { Text("تنظیمات") }
        )
    }
}

/**
 * DashboardScreen - صفحه خانه
 */
@Composable
fun DashboardScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "خانه",
            style = MaterialTheme.typography.headlineMedium
        )
        Text("خلاصه هزینه‌های شما اینجا نمایش داده می‌شود")
    }
}

/**
 * ExpenseListScreen - صفحه هزینه‌ها
 */
@Composable
fun ExpenseListScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "هزینه‌ها",
            style = MaterialTheme.typography.headlineMedium
        )
        Text("لیست هزینه‌های شما اینجا نمایش داده می‌شود")
    }
}

/**
 * ReportsScreen - صفحه گزارش‌ها
 */
@Composable
fun ReportsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "گزارش‌ها",
            style = MaterialTheme.typography.headlineMedium
        )
        Text("گزارش‌های تفصیلی اینجا نمایش داده می‌شود")
    }
}

/**
 * SettingsScreen - صفحه تنظیمات
 */
@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "تنظیمات",
            style = MaterialTheme.typography.headlineMedium
        )
        Text("تنظیمات اپلیکیشن اینجا قرار خواهد گرفت")
    }
}