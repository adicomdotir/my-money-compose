package ir.adicom.mymoney.data.repository

import ir.adicom.mymoney.data.dao.ExpenseDao
import ir.adicom.mymoney.data.entity.Expense
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val expenseDao: ExpenseDao) {

    /**
     * دریافت تمام هزینه‌ها
     */
    fun getAllExpenses(): Flow<List<Expense>> {
        return expenseDao.getAllExpenses()
    }

    /**
     * دریافت هزینه بر اساس ID
     */
    fun getExpenseById(id: Int): Flow<Expense?> {
        return expenseDao.getExpenseById(id)
    }

    /**
     * اضافه کردن هزینه جدید
     */
    suspend fun insertExpense(expense: Expense) {
        expenseDao.insertExpense(expense)
    }

    /**
     * بروزرسانی هزینه
     */
    suspend fun updateExpense(expense: Expense) {
        expenseDao.updateExpense(expense)
    }

    /**
     * حذف هزینه
     */
    suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense)
    }

    /**
     * فیلتر کردن هزینه‌ها بر اساس:
     * - بازه زمانی (startDate، endDate)
     * - دسته‌بندی (categoryId)
     *
     * اگر categoryId = -1 باشد، تمام دسته‌بندی‌ها شامل می‌شوند
     */
    fun getExpensesByFilter(
        startDate: Long,
        endDate: Long,
        categoryId: Int = -1
    ): Flow<List<Expense>> {
        return expenseDao.getExpensesByFilter(startDate, endDate, categoryId)
    }

    /**
     * دریافت کل هزینه‌ها با فیلتر
     */
    fun getTotalExpensesByFilter(
        startDate: Long,
        endDate: Long,
        categoryId: Int = -1
    ): Flow<Double?> {
        return expenseDao.getTotalExpensesByFilter(startDate, endDate, categoryId)
    }
}