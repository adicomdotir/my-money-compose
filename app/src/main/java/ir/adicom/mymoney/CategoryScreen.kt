package ir.adicom.mymoney

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ir.adicom.mymoney.data.entity.Category
import ir.adicom.mymoney.viewmodel.CategoryViewModel

@Composable
fun MyScreen() {
    // ایجاد ViewModel
    val viewModel = viewModel<CategoryViewModel>(
        factory = appViewModelFactory()
    )

    // دریافت State
    val categories by viewModel.categories.collectAsState(emptyList())
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.errorMessage.collectAsState()

    // استفاده در UI
    if (isLoading) LoadingIndicator()
    if (error != null) ErrorDialog(error)

    LazyColumn {
        items(categories) { category ->
            CategoryItem(category=category)
        }
    }

    // فراخوانی Action
    Button(onClick = {
        viewModel.addCategory("خانه", "#FF5733")
    }) {
        Text("اضافه کردن")
    }
}

@Composable
fun CategoryItem(modifier: Modifier = Modifier, category: Category) {

}