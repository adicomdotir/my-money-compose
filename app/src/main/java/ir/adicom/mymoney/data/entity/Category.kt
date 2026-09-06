package ir.adicom.mymoney.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val color: String // رنگ به صورت Hex: #FF5733
)