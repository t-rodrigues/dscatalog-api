package dev.trodrigues.dscatalogapi.services.impl

import dev.trodrigues.dscatalogapi.domain.helpers.buildCategory
import dev.trodrigues.dscatalogapi.domain.helpers.buildProduct
import dev.trodrigues.dscatalogapi.domain.helpers.buildProductRequest
import dev.trodrigues.dscatalogapi.extension.toModel
import dev.trodrigues.dscatalogapi.repositories.CategoryRepository
import dev.trodrigues.dscatalogapi.repositories.ProductRepository
import dev.trodrigues.dscatalogapi.repositories.specifications.ProductSpecification
import dev.trodrigues.dscatalogapi.services.exceptions.DomainException
import dev.trodrigues.dscatalogapi.services.exceptions.ObjectNotFoundException
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.util.*

@ExtendWith(MockKExtension::class)
class ProductServiceImplTest {

    @MockK
    private lateinit var productRepository: ProductRepository

    @MockK
    private lateinit var categoryRepository: CategoryRepository

    @InjectMockKs
    @SpyK
    private lateinit var productService: ProductServiceImpl

    @Test
    fun `should findAll return page of products`() {
        val products = listOf(buildProduct(), buildProduct())
        val pageable: Pageable = PageRequest.of(0, 10)
        val spec = ProductSpecification.getProducts(null)

        every { productRepository.findAll(spec, pageable) } returns PageImpl(products)

        assertDoesNotThrow { productService.findAll(spec, pageable) }
        verify(exactly = 1) { productRepository.findAll(spec, pageable) }
    }

    @Test
    fun `should findAll return page of products with category id 1`() {
        val categoryId = 1L
        val products = listOf(buildProduct(categoryId = categoryId), buildProduct(categoryId = categoryId))
        val pageable: Pageable = PageRequest.of(0, 10)
        val spec = ProductSpecification.getProducts(categoryId)

        every { productRepository.findAll(spec, pageable) } returns PageImpl(products)

        val result = assertDoesNotThrow { productService.findAll(spec, pageable) }
        result.content.forEach { product ->
            val hasCategory = product.categories.find { category -> category.id == categoryId }
            assert(hasCategory?.id == categoryId)
        }
        verify(exactly = 1) { productRepository.findAll(spec, pageable) }
    }

    @Test
    fun `should findById return product when id exists`() {
        val productId = 1L
        val product = buildProduct(id = productId)

        every { productRepository.findById(productId) } returns Optional.of(product)

        val result = assertDoesNotThrow { productService.findById(productId) }
        assert(result.id == productId)
        verify(exactly = 1) { productRepository.findById(productId) }
    }

    @Test
    fun `should findById throw ObjectNotFound when invalid id`() {
        val productId = 100L

        every { productRepository.findById(productId) } returns Optional.empty()

        assertThrows<ObjectNotFoundException> { productService.findById(productId) }
        verify(exactly = 1) { productRepository.findById(productId) }
    }

    @Test
    fun `should create throw DomainException when category id`() {
        val productRequest = buildProductRequest()

        every { categoryRepository.findAllById(any()) } returns listOf()

        assertThrows<DomainException> { productService.create(productRequest) }
        verify(exactly = 1) { categoryRepository.findAllById(any()) }
    }

    @Test
    fun `should create product when valid data`() {
        val categories = listOf(buildCategory(1), buildCategory(2))
        val productRequest = buildProductRequest(categories = categories.map { it.id!! }.toSet())
        val product = productRequest.toModel(categories)

        every { categoryRepository.findAllById(any()) } returns categories
        every { productRepository.save(any()) } returns product

        val result = assertDoesNotThrow { productService.create(productRequest) }
        assert(result.name == product.name)
        assert(result.description == product.description)
        assert(result.price == product.price)
        assert(result.categories == categories)
    }

    @Test
    fun `should update return updated product when valid data`() {
        val categories = listOf(buildCategory(), buildCategory())
        val productRequest = buildProductRequest(name = "Updated")
        val product = productRequest.toModel(buildProduct(), categories)
        val existingId = product.id!!

        every { productService.findById(existingId) } returns product
        every { categoryRepository.findAllById(any()) } returns categories
        every { productRepository.save(any()) } returns product

        val result = assertDoesNotThrow { productService.update(existingId, productRequest) }
        assert(result.id == product.id)
        assert(result.name == "Updated")
    }

    @Test
    fun `should update throw DomainException when invalid categories`() {
        val productRequest = buildProductRequest()
        val product = buildProduct()
        val existingId = product.id!!

        every { productService.findById(existingId) } returns product
        every { categoryRepository.findAllById(any()) } returns listOf()

        assertThrows<DomainException> { productService.update(existingId, productRequest) }
    }

    @Test
    fun `should update throw ObjectNotFound when invalid id`() {
        val productRequest = buildProductRequest()
        val nonExistingId = 100L

        every { productService.findById(nonExistingId) } throws ObjectNotFoundException("")

        assertThrows<ObjectNotFoundException> { productService.update(nonExistingId, productRequest) }
    }

    @Test
    fun `should delete throw ObjectNotFound when invalid id`() {
        val nonExistingId = 10L

        every { productService.findById(nonExistingId) } throws ObjectNotFoundException("")

        assertThrows<ObjectNotFoundException> { productService.delete(nonExistingId) }
        verify(exactly = 0) { productRepository.delete(any()) }
    }

    @Test
    fun `should delete does not throw when valid id`() {
        val existingId = 100L
        val product = buildProduct(id = existingId )

        every { productService.findById(existingId) } returns product
        every { productRepository.delete(product) } just runs

        assertDoesNotThrow { productService.delete(existingId) }
    }

}
