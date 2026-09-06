package ir.adicom.mymoney.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.adicom.mymoney.data.dao.CategoryDao
import ir.adicom.mymoney.data.dao.ExpenseDao
import ir.adicom.mymoney.data.database.AppDatabase
import javax.inject.Singleton

/**
 * Hilt Module برای Database
 * @Singleton - یک نمونه برای کل اپلیکیشن
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * ایجاد Database Instance
     */
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "expense_tracker_db"
        ).build()
    }

    /**
     * ایجاد CategoryDao
     */
    @Singleton
    @Provides
    fun provideCategoryDao(database: AppDatabase): CategoryDao {
        return database.categoryDao()
    }

    /**
     * ایجاد ExpenseDao
     */
    @Singleton
    @Provides
    fun provideExpenseDao(database: AppDatabase): ExpenseDao {
        return database.expenseDao()
    }
}