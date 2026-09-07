package ir.adicom.mymoney.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.adicom.mymoney.data.entity.Category
import ir.adicom.mymoney.data.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    // State برای تمام دسته‌بندی‌ها
    private val _categories = categoryRepository.getAllCategories()
    val categories: Flow<List<Category>> = _categories

    // State برای لودینگ
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // State برای پیام‌های خطا
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // State برای دسته‌بندی انتخاب‌شده (برای ویرایش)
    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory.asStateFlow()

    /**
     * اضافه کردن دسته‌بندی جدید
     */
    fun addCategory(title: String, color: String) {
        if (title.isEmpty()) {
            _errorMessage.value = "عنوان دسته‌بندی نمی‌تواند خالی باشد"
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val category = Category(title = title, color = color)
                categoryRepository.insertCategory(category)
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "خطا در اضافه کردن دسته‌بندی: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * بروزرسانی دسته‌بندی
     */
    fun updateCategory(id: Int, title: String, color: String) {
        if (title.isEmpty()) {
            _errorMessage.value = "عنوان دسته‌بندی نمی‌تواند خالی باشد"
            return
        }

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val category = Category(id = id, title = title, color = color)
                categoryRepository.updateCategory(category)
                _selectedCategory.value = null
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "خطا در بروزرسانی دسته‌بندی: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * حذف دسته‌بندی
     */
    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                categoryRepository.deleteCategory(category)
                _selectedCategory.value = null
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "خطا در حذف دسته‌بندی: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * انتخاب دسته‌بندی برای ویرایش
     */
    fun selectCategory(categoryId: Int) {
        viewModelScope.launch {
            try {
                val category = categoryRepository.getCategoryByIdOnce(categoryId)
                _selectedCategory.value = category
            } catch (e: Exception) {
                _errorMessage.value = "خطا در دریافت دسته‌بندی: ${e.message}"
            }
        }
    }

    /**
     * پاک کردن انتخاب
     */
    fun clearSelection() {
        _selectedCategory.value = null
    }

    /**
     * پاک کردن پیام‌های خطا
     */
    fun clearError() {
        _errorMessage.value = null
    }
}