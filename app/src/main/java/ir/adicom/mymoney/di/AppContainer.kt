package ir.adicom.mymoney.di


import android.content.Context
import ir.adicom.mymoney.data.database.AppDatabase
import ir.adicom.mymoney.data.repository.CategoryRepository
import ir.adicom.mymoney.data.repository.ExpenseRepository

/**
 * Dependency Injection Container
 * برای تهیه Repository‌ها و Database
 */
class AppContainer(context: Context) {

    // Database
    private val database = AppDatabase.getInstance(context)

    // DAOs
    private val categoryDao = database.categoryDao()
    private val expenseDao = database.expenseDao()

    // Repositories
    val categoryRepository = CategoryRepository(categoryDao)
    val expenseRepository = ExpenseRepository(expenseDao)
}