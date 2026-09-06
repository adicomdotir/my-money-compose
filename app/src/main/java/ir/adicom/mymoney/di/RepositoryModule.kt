package ir.adicom.mymoney.di
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.adicom.mymoney.data.dao.CategoryDao
import ir.adicom.mymoney.data.dao.ExpenseDao
import ir.adicom.mymoney.data.repository.CategoryRepository
import ir.adicom.mymoney.data.repository.ExpenseRepository
import javax.inject.Singleton

/**
 * Hilt Module برای Repositories
 * @Singleton - یک نمونه برای کل اپلیکیشن
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * ایجاد CategoryRepository
     */
    @Singleton
    @Provides
    fun provideCategoryRepository(categoryDao: CategoryDao): CategoryRepository {
        return CategoryRepository(categoryDao)
    }

    /**
     * ایجاد ExpenseRepository
     */
    @Singleton
    @Provides
    fun provideExpenseRepository(expenseDao: ExpenseDao): ExpenseRepository {
        return ExpenseRepository(expenseDao)
    }
}