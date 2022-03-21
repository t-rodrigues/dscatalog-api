package dev.trodrigues.dscatalogapi.services.impl

import dev.trodrigues.dscatalogapi.domain.mocks.buildProduct
import dev.trodrigues.dscatalogapi.repositories.CategoryRepository
import dev.trodrigues.dscatalogapi.repositories.ProductRepository
import dev.trodrigues.dscatalogapi.repositories.specifications.ProductSpecification
import dev.trodrigues.dscatalogapi.services.exceptions.ObjectNotFoundException
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
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
    private lateinit var productService: ProductServiceImpl

    @Test
    fun `should return page of products`() {
        val products = listOf(buildProduct(), buildProduct())
        val pageable: Pageable = PageRequest.of(0, 10)
        val spec = ProductSpecification.getProducts(null)

        every { productRepository.findAll(spec, pageable) } returns PageImpl(products)

        assertDoesNotThrow { productService.findAll(spec, pageable) }
        verify(exactly = 1) { productRepository.findAll(spec, pageable) }
    }

    @Test
    fun `should return page of products with category id 1`() {
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
    fun `should find by id returns product when id exists`() {
        val productId = 1L
        val product = buildProduct(id = productId)

        every { productRepository.findById(productId) } returns Optional.of(product)

        val result = assertDoesNotThrow { productService.findById(productId) }
        assert(result.id == productId)
        verify(exactly = 1) { productRepository.findById(productId) }
    }

    @Test
    fun `should throw ObjectNotFound when invalid id`() {
        val productId = 100L

        every { productRepository.findById(productId) } returns Optional.empty()

        assertThrows<ObjectNotFoundException> { productService.findById(productId) }
        verify(exactly = 1) { productRepository.findById(productId) }
    }

}
