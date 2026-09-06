package ir.adicom.mymoney.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ir.adicom.mymoney.data.repository.CategoryRepository
import ir.adicom.mymoney.data.repository.ExpenseRepository
import kotlin.jvm.java

/**
 * Factory برای ایجاد ViewModels
 * استفاده:
 * val viewModel = ViewModelProvider(this, factory).get(CategoryViewModel::class.java)
 */
class ViewModelFactory(
    private val categoryRepository: CategoryRepository,
    private val expenseRepository: ExpenseRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            CategoryViewModel::class.java -> {
                @Suppress("UNCHECKED_CAST")
                CategoryViewModel(categoryRepository) as T
            }

//            ExpenseViewModel::class.java -> {
//                @Suppress("UNCHECKED_CAST")
//                ExpenseViewModel(expenseRepository) as T
//            }
//
//            ReportViewModel::class.java -> {
//                @Suppress("UNCHECKED_CAST")
//                ReportViewModel(expenseRepository, categoryRepository) as T
//            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}