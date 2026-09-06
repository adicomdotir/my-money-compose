package ir.adicom.mymoney.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ir.adicom.mymoney.data.entity.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert
    suspend fun insertExpense(expense: Expense)

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY timestamp DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE id = :id")
    fun getExpenseById(id: Int): Flow<Expense?>

    @Query("""
        SELECT * FROM expenses 
        WHERE timestamp BETWEEN :startDate AND :endDate
        AND (:categoryId = -1 OR categoryId = :categoryId)
        ORDER BY timestamp DESC
    """)
    fun getExpensesByFilter(
        startDate: Long,
        endDate: Long,
        categoryId: Int = -1
    ): Flow<List<Expense>>

    @Query("""
        SELECT SUM(price) FROM expenses 
        WHERE timestamp BETWEEN :startDate AND :endDate
        AND (:categoryId = -1 OR categoryId = :categoryId)
    """)
    fun getTotalExpensesByFilter(
        startDate: Long,
        endDate: Long,
        categoryId: Int = -1
    ): Flow<Double?>
}