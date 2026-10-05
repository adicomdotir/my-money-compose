package ir.adicom.mymoney.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ir.adicom.mymoney.data.dto.CategorySummaryDto
import ir.adicom.mymoney.data.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert
    suspend fun addTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Long): TransactionEntity?

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Query("SELECT SUM(amount) FROM transactions WHERE type = :type AND timestamp >= :startTime")
    fun getTotalAmountSince(type: String, startTime: Long): Flow<Double?>

    @Query("""
        SELECT category, SUM(amount) AS totalAmount 
        FROM transactions 
        WHERE type = :type AND timestamp >= :startTime 
        GROUP BY category 
        ORDER BY totalAmount DESC
    """)
    fun getCategorySummariesSince(type: String, startTime: Long): Flow<List<CategorySummaryDto>>
}

