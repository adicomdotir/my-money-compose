package ir.adicom.mymoney.data.mapper

import ir.adicom.mymoney.data.dto.CategorySummaryDto
import ir.adicom.mymoney.domain.model.CategorySummary

fun CategorySummaryDto.toDomain(): CategorySummary = CategorySummary(
    category = category,
    totalAmount = totalAmount
)