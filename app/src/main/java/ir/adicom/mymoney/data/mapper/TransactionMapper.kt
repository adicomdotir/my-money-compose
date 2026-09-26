package ir.adicom.mymoney.data.mapper

import ir.adicom.mymoney.data.entity.TransactionEntity
import ir.adicom.mymoney.domain.model.Transaction
import ir.adicom.mymoney.domain.model.TransactionType

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