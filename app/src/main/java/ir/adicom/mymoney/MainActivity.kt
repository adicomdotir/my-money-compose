package ir.adicom.mymoney

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import ir.adicom.mymoney.data.database.AppDatabase
import ir.adicom.mymoney.ui.theme.MyMoneyTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyMoneyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    AppNavHost(applicationContext = applicationContext)
                }
            }
        }
    }
}

sealed class AppScreen(val route: String) {
    data object Home : AppScreen("home")
    data object AddTransaction : AppScreen("add_transaction")
    data object Detail : AppScreen("detail/{id}")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    applicationContext: Context
) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.Home.route
    ) {
        composable(AppScreen.Home.route) {
            TransactionScreen(
                onOpenAdd = {
                    navController.navigate(AppScreen.AddTransaction.route)
                },
                onDetailClick = { id ->
                    navController.navigate("detail/$id")
                }
            )
        }
        composable(AppScreen.AddTransaction.route) {
            AddTransactionScreen()
        }
        composable(
            AppScreen.Detail.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: return@composable
            TransactionDetailScreen(id = id)
        }
    }


}