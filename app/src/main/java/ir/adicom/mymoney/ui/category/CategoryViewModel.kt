package ir.adicom.mymoney.ui.category

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

data class CategoryUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedCategory: Category? = null
)

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _categories = categoryRepository.getAllCategories()
    val categories: Flow<List<Category>> = _categories

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    fun addCategory(title: String, color: String) {
        if (title.isEmpty()) {
            _uiState.value = _uiState.value.copy(errorMessage = "عنوان دسته‌بندی نمی‌تواند خالی باشد")
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val category = Category(title = title, color = color)
                categoryRepository.insertCategory(category)
                _uiState.value = _uiState.value.copy(errorMessage = null)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = "خطا در اضافه کردن دسته‌بندی: ${e.message}")
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun updateCategory(id: Int, title: String, color: String) {
        if (title.isEmpty()) {
            _uiState.value = _uiState.value.copy(errorMessage = "عنوان دسته‌بندی نمی‌تواند خالی باشد")
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val category = Category(id = id, title = title, color = color)
                categoryRepository.updateCategory(category)
                _uiState.value = _uiState.value.copy(selectedCategory = null, errorMessage = null)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = "خطا در بروزرسانی دسته‌بندی: ${e.message}")
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                categoryRepository.deleteCategory(category)
                _uiState.value = _uiState.value.copy(selectedCategory = null, errorMessage = null)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = "خطا در حذف دسته‌بندی: ${e.message}")
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun selectCategory(categoryId: Int) {
        viewModelScope.launch {
            try {
                val category = categoryRepository.getCategoryByIdOnce(categoryId)
                _uiState.value = _uiState.value.copy(selectedCategory = category)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = "خطا در دریافت دسته‌بندی: ${e.message}")
            }
        }
    }

    fun clearSelection() {
        _uiState.value = _uiState.value.copy(selectedCategory = null)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}