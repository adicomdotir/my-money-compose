package ir.adicom.mymoney.data.database


import androidx.room.Database
import androidx.room.RoomDatabase
import ir.adicom.mymoney.data.dao.CategoryDao
import ir.adicom.mymoney.data.dao.ExpenseDao
import ir.adicom.mymoney.data.dao.TransactionDao
import ir.adicom.mymoney.data.entity.Category
import ir.adicom.mymoney.data.entity.Expense
import ir.adicom.mymoney.data.entity.TransactionEntity

@Database(
    entities = [TransactionEntity::class, Category::class, Expense::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao
}