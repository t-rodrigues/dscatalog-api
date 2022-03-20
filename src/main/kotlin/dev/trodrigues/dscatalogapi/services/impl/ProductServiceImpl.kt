package dev.trodrigues.dscatalogapi.services.impl

import dev.trodrigues.dscatalogapi.domain.Product
import dev.trodrigues.dscatalogapi.extension.toModel
import dev.trodrigues.dscatalogapi.repositories.CategoryRepository
import dev.trodrigues.dscatalogapi.repositories.ProductRepository
import dev.trodrigues.dscatalogapi.resources.requests.ProductRequest
import dev.trodrigues.dscatalogapi.services.ProductService
import dev.trodrigues.dscatalogapi.services.exceptions.DomainException
import dev.trodrigues.dscatalogapi.services.exceptions.ObjectNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) : ProductService {

    override fun findAll(spec: Specification<Product>, pageable: Pageable): Page<Product> =
        productRepository.findAll(spec, pageable)

    override fun findById(productId: Long): Product =
        productRepository.findById(productId)
            .orElseThrow { ObjectNotFoundException("Product [$productId] not found") }

    @Transactional
    override fun create(productRequest: ProductRequest): Product {
        val categories = categoryRepository.findAllById(productRequest.categories.map { it.id })
        if (categories.isEmpty()) {
            throw DomainException("Product must have at least one valid category")
        }
        val product = productRequest.toModel(categories)
        return productRepository.save(product)
    }

    @Transactional
    override fun update(productId: Long, productRequest: ProductRequest): Product {
        val oldProduct = productRepository.findById(productId).orElseThrow { DomainException("Product not found [$productId]") }
        val categories = categoryRepository.findAllById(productRequest.categories.map { it.id })
        val updatedProduct = productRequest.toModel(oldProduct, categories)
        return productRepository.save(updatedProduct)
    }

    @Transactional
    override fun delete(productId: Long) {
        val product = findById(productId)
        productRepository.delete(product)
    }

}
