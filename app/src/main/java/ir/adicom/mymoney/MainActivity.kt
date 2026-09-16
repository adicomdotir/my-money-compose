package ir.adicom.mymoney

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import ir.adicom.mymoney.data.database.AppDatabase
import ir.adicom.mymoney.ui.screens.HomeScreen
import ir.adicom.mymoney.ui.theme.MyMoneyTheme
import ir.adicom.mymoney.ui.viewmodel.CategoryViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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
                    val database = AppDatabase.getInstance(applicationContext)
                    val repository = TransactionRepository(database.transactionDao())
                    val viewModel = TransactionViewModel(repository)
                    TransactionScreen(viewModel = viewModel)
                }
            }
        }
    }
}