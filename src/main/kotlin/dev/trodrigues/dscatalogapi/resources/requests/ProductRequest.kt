package dev.trodrigues.dscatalogapi.resources.requests

import java.time.LocalDateTime
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class ProductRequest(
    @field:NotBlank
    val name: String,
    @field:NotBlank
    val description: String,
    @field:DecimalMin("0.00")
    val price: Double,
    val imageUrl: String?,
    val date: LocalDateTime?,
    @field:NotNull
    val categories: Set<ProductCategory>
)

data class ProductCategory(
    val id: Long
)
