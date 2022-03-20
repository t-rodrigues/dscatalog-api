package dev.trodrigues.dscatalogapi.services.impl

import dev.trodrigues.dscatalogapi.domain.Category
import dev.trodrigues.dscatalogapi.extension.toModel
import dev.trodrigues.dscatalogapi.repositories.CategoryRepository
import dev.trodrigues.dscatalogapi.repositories.ProductRepository
import dev.trodrigues.dscatalogapi.resources.requests.PutCategoryRequest
import dev.trodrigues.dscatalogapi.services.CategoryService
import dev.trodrigues.dscatalogapi.services.exceptions.DomainException
import dev.trodrigues.dscatalogapi.services.exceptions.ObjectNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository
) : CategoryService {

    override fun findAll(pageable: Pageable): Page<Category> = categoryRepository.findAll(pageable)

    override fun findById(categoryId: Long): Category =
        categoryRepository.findById(categoryId)
            .orElseThrow { ObjectNotFoundException("Category $categoryId not found") }

    @Transactional
    override fun create(category: Category): Category =
        categoryRepository.save(category)

    @Transactional
    override fun update(categoryId: Long, putCategoryRequest: PutCategoryRequest): Category {
        val oldCategory = findById(categoryId)
        val updatedCategory = putCategoryRequest.toModel(oldCategory)
        return categoryRepository.save(updatedCategory)
    }

    @Transactional
    override fun delete(categoryId: Long) {
        val category = findById(categoryId)
        if(productRepository.existsByCategories(category)) {
            throw DomainException("Category cannot be deleted, category($categoryId) has product registered")
        }
        categoryRepository.delete(category)
    }

}
