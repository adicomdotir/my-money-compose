package ir.adicom.mymoney

import ir.adicom.mymoney.data.entity.TransactionEntity

data class Transaction(
    val id: Long,
    val title: String,
    val category: String,
    val amount: Double,
    val type: TransactionType
)

fun Transaction.toEntity(): TransactionEntity = TransactionEntity(
    id = id,
    title = title,
    category = category,
    amount = amount,
    type = type.name
)

fun TransactionEntity.toDomain(): Transaction = Transaction(
    id = id,
    title = title,
    category = category,
    amount = amount,
    type = runCatching {
        TransactionType.valueOf(type)
    }.getOrDefault(TransactionType.EXPENSE)
)