package ir.adicom.mymoney.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ir.adicom.mymoney.ui.addtransaction.AddTransactionScreen
import ir.adicom.mymoney.ui.detail.TransactionDetailScreen
import ir.adicom.mymoney.ui.edittransaction.EditTransactionScreen
import ir.adicom.mymoney.ui.home.DrawerItem
import ir.adicom.mymoney.ui.home.HomeScreen
import ir.adicom.mymoney.ui.report.ReportScreen
import ir.adicom.mymoney.ui.setting.SettingScreen
import ir.adicom.mymoney.ui.transaction.TransactionScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onDrawerClick = {
                    when(it) {
                        DrawerItem.Report -> navController.navigate(Screen.Report.route)
                        DrawerItem.Setting -> navController.navigate(Screen.Setting.route)
                        DrawerItem.Transaction -> navController.navigate(Screen.Transaction.route)
                    }
                },
            )
        }

        composable(Screen.Transaction.route) {
            TransactionScreen(
                onOpenAdd = {
                    navController.navigate(Screen.AddTransaction.route)
                },
                onDetailClick = { id ->
                    navController.navigate(Screen.Detail.createRoute(id))
                }
            )
        }

        composable(Screen.AddTransaction.route) {
            AddTransactionScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            Screen.EditTransaction.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )
        ) {
            EditTransactionScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            Screen.Detail.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            TransactionDetailScreen(
                onEdit = { id ->
                    navController.navigate(Screen.EditTransaction.createRoute(id))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            Screen.Report.route
        ) {
            ReportScreen(
                onBackClick = {
                    navController.navigate(Screen.Setting.route)
                }
            )
        }

        composable(
            Screen.Setting.route
        ) {
            SettingScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }


}