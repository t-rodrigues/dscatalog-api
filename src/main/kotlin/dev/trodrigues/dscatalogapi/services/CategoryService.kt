package dev.trodrigues.dscatalogapi.services

import dev.trodrigues.dscatalogapi.domain.Category
import dev.trodrigues.dscatalogapi.resources.requests.PutCategoryRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CategoryService {

    fun findAll(pageable: Pageable): Page<Category>

    fun findById(id: Long): Category

    fun create(category: Category): Category

    fun update(categoryId: Long, putCategoryRequest: PutCategoryRequest): Category

    fun delete(categoryId: Long)

}
