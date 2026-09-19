package ir.adicom.mymoney

import android.annotation.SuppressLint
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.adicom.mymoney.data.database.AppDatabase
import ir.adicom.mymoney.ui.theme.MyMoneyTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyMoneyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    AppNavHost(applicationContext=applicationContext)
                }
            }
        }
    }
}

sealed class AppScreen(val route: String) {
    data object Home : AppScreen("home")
    data object AddTransaction : AppScreen("add_transaction")
}

@SuppressLint("ViewModelConstructorInComposable")
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
            val database = AppDatabase.getInstance(applicationContext)
            val repository = TransactionRepository(database.transactionDao())

            val viewModel: TransactionViewModel = viewModel(
                factory = TransactionViewModelFactory(repository)
            )
            TransactionScreen(viewModel = viewModel, onOpenAdd = {
                navController.navigate(AppScreen.AddTransaction.route)
            })
        }
        composable(AppScreen.AddTransaction.route) {
            AddTransactionScreen()
        }
    }
}