package dev.trodrigues.dscatalogapi.resources.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CategoryResponse(
    val id: Long? = null,
    val name: String
)
