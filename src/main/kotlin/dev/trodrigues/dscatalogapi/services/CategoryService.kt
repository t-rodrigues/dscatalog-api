package dev.trodrigues.dscatalogapi.services

import dev.trodrigues.dscatalogapi.domain.Category

interface CategoryService {

    fun findAll(): List<Category>

    fun findById(id: Long): Category

    fun create(category: Category): Category

    fun update(category: Category): Category

}
