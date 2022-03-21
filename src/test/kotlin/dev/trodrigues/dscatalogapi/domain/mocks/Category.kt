package dev.trodrigues.dscatalogapi.domain.mocks

import dev.trodrigues.dscatalogapi.domain.Category
import java.util.*

fun buildCategory(id: Long = Random().nextLong(), name: String = "${UUID.randomUUID()}"): Category = Category(
    id = id,
    name = name
)
