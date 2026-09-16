package ir.adicom.mymoney

import ir.adicom.mymoney.data.dao.TransactionDao
import ir.adicom.mymoney.data.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class TransactionRepository(
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
        transactionDao.addTransaction(transaction.toEntity())
    }
}