package ir.adicom.mymoney

import ir.adicom.mymoney.data.dao.TransactionDao
import ir.adicom.mymoney.data.entity.TransactionEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao
) {
    fun getTransactions(): Flow<List<Transaction>> {
        return transactionDao.getAllTransactions()
            .map { entities ->
                entities.map(TransactionEntity::toDomain)
            }
    }

    suspend fun addTransaction(
        transaction: Transaction
    ) {
        delay(2000L)
        transactionDao.addTransaction(transaction.toEntity())
    }

    suspend fun deleteTransaction(transaction: Transaction) {
        delay(2000L)
        transactionDao.deleteTransaction(transaction.toEntity())
    }
}