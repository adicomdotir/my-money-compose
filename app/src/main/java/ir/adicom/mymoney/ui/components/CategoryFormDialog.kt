package ir.adicom.mymoney.ui.components

import ir.adicom.mymoney.data.entity.Category
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt

/**
 * CategoryFormDialog - دیالوگ دسته‌بندی
 */
@Composable
fun CategoryFormDialog(
    category: Category?,
    isEditMode: Boolean,
    onDismiss: () -> Unit,
    onSave: (title: String, color: String) -> Unit
) {
    // Form State
    var title by remember { mutableStateOf(category?.title ?: "") }
    var selectedColor by remember { mutableStateOf(category?.color ?: "#FF5733") }

    // رنگ‌های از پیش تعریف شده
    val predefinedColors = listOf(
        "#FF5733" to "قرمز",
        "#FFC300" to "زرد",
        "#33FF57" to "سبز",
        "#3366FF" to "آبی",
        "#FF33F5" to "بنفش",
        "#33FFF5" to "فیروزه‌ای",
        "#FF9800" to "نارنجی",
        "#9C27B0" to "بنفش گیاه",
        "#2196F3" to "آبی روشن",
        "#4CAF50" to "سبز درخت"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isEditMode) "ویرایش دسته‌بندی" else "دسته‌بندی جدید",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title Input
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("نام دسته‌بندی") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                // Color Selection
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "انتخاب رنگ",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(predefinedColors) { (color, label) ->
                            ColorOption(
                                color = color,
                                label = label,
                                isSelected = selectedColor == color,
                                onClick = { selectedColor = color }
                            )
                        }
                    }

                    // Custom Color Input
                    OutlinedTextField(
                        value = selectedColor,
                        onValueChange = { selectedColor = it },
                        label = { Text("رنگ سفارشی (Hex: #RRGGBB)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii)
                    )
                }

                // Selected Color Preview
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedColor,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = try {
                                    Color(selectedColor.toColorInt())
                                } catch (e: Exception) {
                                    Color.Gray
                                },
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = RoundedCornerShape(8.dp)
                            )
                    )
                    Text(
                        text = "پیش‌نمایش:",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(title, selectedColor)
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text(if (isEditMode) "به‌روزرسانی" else "اضافه کردن")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("لغو")
            }
        },
        modifier = Modifier.fillMaxWidth(0.9f)
    )
}

/**
 * ColorOption - یک گزینه رنگ
 */
@Composable
fun ColorOption(
    color: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(if (isSelected) 48.dp else 40.dp)
                .background(
                    color = try {
                        Color(color.toColorInt())
                    } catch (e: Exception) {
                        Color.Gray
                    },
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = if (isSelected) 3.dp else 1.dp,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outline
                    },
                    shape = RoundedCornerShape(8.dp)
                )
        )

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontSize = MaterialTheme.typography.labelSmall.fontSize * 0.8
        )
    }
}