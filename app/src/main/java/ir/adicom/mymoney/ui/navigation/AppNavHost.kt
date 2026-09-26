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
            AddTransactionScreen(onBack = {
                navController.popBackStack()
            })
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
            )) { backStackEntry ->
            TransactionDetailScreen(
                onEdit = { id ->
                    navController.navigate(Screen.EditTransaction.createRoute(id))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }


}