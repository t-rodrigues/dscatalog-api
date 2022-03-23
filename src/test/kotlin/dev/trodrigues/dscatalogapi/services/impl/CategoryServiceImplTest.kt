package dev.trodrigues.dscatalogapi.services.impl

import dev.trodrigues.dscatalogapi.domain.mocks.buildCategory
import dev.trodrigues.dscatalogapi.domain.mocks.buildCategoryRequest
import dev.trodrigues.dscatalogapi.extension.toModel
import dev.trodrigues.dscatalogapi.repositories.CategoryRepository
import dev.trodrigues.dscatalogapi.repositories.ProductRepository
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
import java.util.*

@ExtendWith(MockKExtension::class)
class CategoryServiceImplTest {

    @MockK
    private lateinit var categoryRepository: CategoryRepository

    @MockK
    private lateinit var productRepository: ProductRepository

    @InjectMockKs
    @SpyK
    private lateinit var categoryService: CategoryServiceImpl

    @Test
    fun `should findAll return page of categories`() {
        val categories = listOf(buildCategory(), buildCategory())
        val pageable = PageRequest.ofSize(10)

        every { categoryRepository.findAll(pageable) } returns PageImpl(categories)

        val response = assertDoesNotThrow { categoryService.findAll(pageable) }
        assert(response.content.size == 2)
        verify(exactly = 1) { categoryRepository.findAll(pageable) }
    }

    @Test
    fun `should findById throws ObjectNotFoundException when invalid id`() {
        val nonExistingId = 100L

        every { categoryRepository.findById(nonExistingId) } returns Optional.empty()

        assertThrows<ObjectNotFoundException> { categoryService.findById(nonExistingId) }
        verify(exactly = 1) { categoryRepository.findById(nonExistingId) }
    }

    @Test
    fun `should findById returns category when valid id`() {
        val category = buildCategory()
        val existingId = category.id!!

        every { categoryRepository.findById(existingId) } returns Optional.of(category)

        val response = assertDoesNotThrow { categoryService.findById(existingId) }
        assert(response.id == category.id)
        assert(response.name == category.name)
    }

    @Test
    fun `should create return category when valid data`() {
        val category = buildCategory()

        every { categoryRepository.save(category) } returns category

        val response = assertDoesNotThrow { categoryService.create(category) }
        assert(response.id != null)
        assert(response.name == category.name)
    }

    @Test
    fun `should update throws when invalid id was provided`() {
        val categoryRequest = buildCategoryRequest()
        val nonExistingId = 100L

        every { categoryService.findById(nonExistingId) } throws ObjectNotFoundException("")

        assertThrows<ObjectNotFoundException> { categoryService.update(nonExistingId, categoryRequest) }
        verify(exactly = 1) { categoryService.findById(nonExistingId) }
    }

    @Test
    fun `should update returns updated category when valid data`() {
        val categoryRequest = buildCategoryRequest(name = "Updated")
        val category = categoryRequest.toModel(buildCategory())
        val existingId = category.id!!

        every { categoryService.findById(existingId) } returns category
        every { categoryRepository.save(category) } returns category

        val response = assertDoesNotThrow { categoryService.update(existingId, categoryRequest) }
        assert(response.id == existingId)
        assert(response.name == "Updated")
    }

    @Test
    fun `should delete throws ObjectNotFound when invalid id`() {
        val nonExistingId = 100L

        every { categoryService.findById(nonExistingId) } throws ObjectNotFoundException("")

        assertThrows<ObjectNotFoundException> { categoryService.delete(nonExistingId) }
        verify(exactly = 1) { categoryService.findById(nonExistingId) }
    }

    @Test
    fun `should delete throws DomainException when product has relation with category`() {
        val category = buildCategory()
        val existingId = category.id!!

        every { categoryService.findById(existingId) } returns category
        every { productRepository.existsByCategories(category) } returns true

        assertThrows<DomainException> { categoryService.delete(existingId) }
    }

    @Test
    fun `should return nothing when delete successful`() {
        val category = buildCategory()
        val existingId = category.id!!

        every { categoryService.findById(existingId) } returns category
        every { productRepository.existsByCategories(category) } returns false
        every { categoryRepository.delete(category) } just runs

        assertDoesNotThrow { categoryService.delete(existingId) }
        verify(exactly = 1) { categoryRepository.delete(category) }
    }

}
