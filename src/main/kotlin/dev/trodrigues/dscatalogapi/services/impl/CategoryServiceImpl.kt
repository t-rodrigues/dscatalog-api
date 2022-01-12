package dev.trodrigues.dscatalogapi.services.impl

import dev.trodrigues.dscatalogapi.domain.Category
import dev.trodrigues.dscatalogapi.repositories.CategoryRepository
import dev.trodrigues.dscatalogapi.services.CategoryService
import dev.trodrigues.dscatalogapi.services.exceptions.ObjectNotFoundException
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository
) : CategoryService {

    override fun findAll(): List<Category> = categoryRepository.findAll()

    override fun findById(categoryId: Long): Category =
        categoryRepository.findById(categoryId)
            .orElseThrow { ObjectNotFoundException("Category $categoryId not found") }

    override fun create(category: Category): Category =
        categoryRepository.save(category)

    override fun update(category: Category): Category {
        findById(category.id!!)
        return categoryRepository.save(category)
    }

    override fun delete(categoryId: Long) {
        val category = findById(categoryId)
        categoryRepository.delete(category)
    }

}
