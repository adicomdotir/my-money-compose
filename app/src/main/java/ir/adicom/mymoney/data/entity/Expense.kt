package ir.adicom.mymoney.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val price: Double,
    val timestamp: Long, // تاریخ به صورت Timestamp (میلادی)
    val categoryId: Int
)