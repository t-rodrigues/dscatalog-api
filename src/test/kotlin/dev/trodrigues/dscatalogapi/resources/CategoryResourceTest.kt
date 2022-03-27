package dev.trodrigues.dscatalogapi.resources

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import dev.trodrigues.dscatalogapi.domain.helpers.buildCategory
import dev.trodrigues.dscatalogapi.domain.helpers.buildCategoryRequest
import dev.trodrigues.dscatalogapi.extension.toModel
import dev.trodrigues.dscatalogapi.services.CategoryService
import dev.trodrigues.dscatalogapi.services.exceptions.ObjectNotFoundException
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.PageImpl
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(controllers = [CategoryResource::class])
class CategoryResourceTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var categoryService: CategoryService

    private val BASE_URL = "/categories"

    @Test
    fun `should getCategories return an page of categories`() {
        val page = PageImpl(listOf(buildCategory()))

        every { categoryService.findAll(any()) } returns page

        mockMvc.perform(get(BASE_URL))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.items[0].id").exists())
    }

    @Test
    fun `should getCategoryById returns 404 when invalid id`() {
        val nonExistingId = 300L

        every { categoryService.findById(nonExistingId) } throws ObjectNotFoundException("")

        mockMvc.perform(get("$BASE_URL/{categoryId}", nonExistingId))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should getCategoryById returns an category when valid id`() {
        val category = buildCategory()
        val existingId = category.id!!

        every { categoryService.findById(existingId) } returns category

        mockMvc.perform(get("$BASE_URL/{categoryId}", existingId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(category.id))
    }

    @Test
    fun `should createCategory return 400 when invalid data`() {
        val categoryRequest = buildCategoryRequest(name = "")

        mockMvc.perform(
            post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest))
        )
            .andExpect(status().isBadRequest)
        verify(exactly = 0) { categoryService.create(any()) }
    }

    @Test
    fun `should createCategory return 201 when valid data`() {
        val categoryRequest = buildCategoryRequest()
        val category = buildCategory()

        every { categoryService.create(any()) } returns category

        mockMvc.perform(
            post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest))
        )
            .andExpect(header().exists("Location"))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
    }

    @Test
    fun `should updateCategory return 404 when invalid id`() {
        val nonExistingId = 100L
        val categoryRequest = buildCategoryRequest()

        every { categoryService.update(nonExistingId, any()) } throws ObjectNotFoundException("")

        mockMvc.perform(
            put("$BASE_URL/{categoryId}", nonExistingId).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should updateCategory returns 400 when invalid data`() {
        val existingId = 1L
        val categoryRequest = buildCategoryRequest(name = "")

        mockMvc.perform(
            put("$BASE_URL/{categoryId}", existingId).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should updateCategory returns updated category`() {
        val existingId = 1L
        val categoryRequest = buildCategoryRequest(name = "Updated")
        val category = categoryRequest.toModel(buildCategory(id = existingId))

        every { categoryService.update(existingId, any()) } returns category

        mockMvc.perform(
            put("$BASE_URL/{categoryId}", existingId).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(existingId))
            .andExpect(jsonPath("$.name").value("Updated"))
    }

    @Test
    fun `should deleteCategory returns 404 when invalid id`() {
        val nonExistingId = 100L

        every { categoryService.delete(nonExistingId) } throws ObjectNotFoundException("")

        mockMvc.perform(delete("$BASE_URL/{categoryId}", nonExistingId)).andExpect(status().isNotFound)
    }

    @Test
    fun `should deleteCategory returns 201 when successful`() {
        val existingId = 1L

        every { categoryService.delete(existingId) } just runs

        mockMvc.perform(delete("$BASE_URL/{categoryId}", existingId)).andExpect(status().isNoContent)
    }

}
