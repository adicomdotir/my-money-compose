package ir.adicom.mymoney.data.repository

import ir.adicom.mymoney.data.dao.CategoryDao
import ir.adicom.mymoney.data.entity.Category
import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val categoryDao: CategoryDao) {

    /**
     * دریافت تمام دسته‌بندی‌ها
     */
    fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories()
    }

    /**
     * دریافت دسته‌بندی بر اساس ID
     */
    fun getCategoryById(id: Int): Flow<Category?> {
        return categoryDao.getCategoryById(id)
    }

    /**
     * اضافه کردن دسته‌بندی جدید
     */
    suspend fun insertCategory(category: Category) {
        categoryDao.insertCategory(category)
    }

    /**
     * بروزرسانی دسته‌بندی
     */
    suspend fun updateCategory(category: Category) {
        categoryDao.updateCategory(category)
    }

    /**
     * حذف دسته‌بندی
     */
    suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
    }

    /**
     * دریافت دسته‌بندی (بدون Flow - برای استفاده فوری)
     */
    suspend fun getCategoryByIdOnce(id: Int): Category? {
        return categoryDao.getCategoryByIdOnce(id)
    }
}