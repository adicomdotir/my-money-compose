package ir.adicom.mymoney.ui.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.adicom.mymoney.R
import ir.adicom.mymoney.data.entity.Category
import ir.adicom.mymoney.ui.components.AppIconButton
import ir.adicom.mymoney.ui.components.DeleteConfirmDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
    viewModel: CategoryViewModel = hiltViewModel(),
) {
    val categories by viewModel.categories.collectAsStateWithLifecycle(emptyList())
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Dialog / pending-delete state — local to the screen, reset on dispose
    var showFormDialog by rememberSaveable { mutableStateOf(false) }
    var categoryPendingDelete by remember { mutableStateOf<Category?>(null) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.categories_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    Text(
                        text = stringResource(R.string.categories_count, categories.size),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    viewModel.clearSelection()
                    showFormDialog = true
                },
                icon = { Icon(Icons.Outlined.Add, contentDescription = null) },
                text = { Text(stringResource(R.string.add_category)) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            uiState.errorMessage?.let { message ->
                ErrorBanner(
                    message = message,
                    onDismiss = viewModel::clearError,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                when {
                    uiState.isLoading -> LoadingState()
                    categories.isEmpty() -> EmptyState(
                        onAddClick = {
                            viewModel.clearSelection()
                            showFormDialog = true
                        }
                    )
                    else -> CategoryList(
                        categories = categories,
                        onEdit = { category ->
                            viewModel.selectCategory(category.id)
                            showFormDialog = true
                        },
                        onDeleteClick = { categoryPendingDelete = it }
                    )
                }
            }
        }
    }

    // ---------- Delete confirmation ----------
    categoryPendingDelete?.let { category ->
        DeleteConfirmDialog(
            title = stringResource(R.string.delete_category_title),
            message = stringResource(R.string.delete_category_message, category.title),
            isLoading = uiState.isLoading,
            onConfirm = {
                viewModel.deleteCategory(category)
                categoryPendingDelete = null
            },
            onDismiss = { categoryPendingDelete = null }
        )
    }

    // ---------- Add / Edit form dialog ----------
    if (showFormDialog) {
        CategoryFormDialog(
            category = uiState.selectedCategory,
            isEditMode = uiState.selectedCategory != null,
            onDismiss = {
                showFormDialog = false
                viewModel.clearSelection()
            },
            onSave = { title, color ->
                uiState.selectedCategory
                    ?.let { viewModel.updateCategory(it.id, title, color) }
                    ?: viewModel.addCategory(title, color)
                showFormDialog = false
                viewModel.clearSelection()
            }
        )
    }
}

/* ==================== Sub-composables ==================== */

@Composable
private fun CategoryList(
    categories: List<Category>,
    onEdit: (Category) -> Unit,
    onDeleteClick: (Category) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 8.dp,
            bottom = 96.dp // FAB safe area
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories, key = { it.id }) { category ->
            CategoryListItem(
                category = category,
                onEdit = { onEdit(category) },
                onDeleteClick = { onDeleteClick(category) }
            )
        }
    }
}

@Composable
fun CategoryListItem(
    category: Category,
    onEdit: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CategoryColorDot(color = category.color)

            // Title + optional subtitle
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Edit
            AppIconButton(
                icon = Icons.Outlined.Edit,
                contentDescription = stringResource(R.string.edit_category_cd, category.title),
                tint = MaterialTheme.colorScheme.primary,
                size = AppIconButtonSize.Small,
                onClick = onEdit
            )

            // Delete
            AppIconButton(
                icon = Icons.Outlined.Delete,
                contentDescription = stringResource(R.string.delete_category_cd, category.title),
                tint = MaterialTheme.colorScheme.error,
                size = AppIconButtonSize.Small,
                onClick = onDeleteClick
            )
        }
    }
}

@Composable
private fun CategoryColorDot(color: String, modifier: Modifier = Modifier) {
    val parsed = remember(color) { color.toColorSafely() }
    Box(
        modifier = modifier
            .size(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(parsed)
    )
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun EmptyState(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Outlined.Category,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.no_categories_title),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.no_categories_message),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = onAddClick) {
            Icon(Icons.Outlined.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.add_category))
        }
    }
}

@Composable
private fun ErrorBanner(
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.errorContainer
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.weight(1f)
            )
            AppIconButton(
                icon = Icons.Outlined.Close,
                contentDescription = stringResource(R.string.dismiss_error),
                tint = MaterialTheme.colorScheme.onErrorContainer,
                size = AppIconButtonSize.Small,
                onClick = onDismiss
            )
        }
    }
}

/* ==================== Safe color parsing ==================== */

private fun String.toColorSafely(fallback: Color = Color.Gray): Color =
    runCatching { Color(this.toColorInt()) }.getOrDefault(fallback)