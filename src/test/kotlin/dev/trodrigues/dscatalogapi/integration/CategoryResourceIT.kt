package dev.trodrigues.dscatalogapi.integration

import com.fasterxml.jackson.databind.ObjectMapper
import dev.trodrigues.dscatalogapi.domain.helpers.buildCategoryRequest
import dev.trodrigues.dscatalogapi.domain.helpers.buildProductRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class CategoryResourceIT {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val BASE_URL = "/categories"

    @Test
    fun `should getCategories return an page of categories`() {
        mockMvc.perform(get(BASE_URL)).andExpect(status().isOk)
            .andExpect(jsonPath("$.items").isNotEmpty)
            .andExpect(jsonPath("$.items[*].id").exists())
    }

    @Test
    fun `should getCategoryById return 404 when invalid id`() {
        val nonExistingId = 100

        mockMvc.perform(get("$BASE_URL/{categoryId}", nonExistingId))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should getCategoryById return category when valid id`() {
        val existingId = 1

        mockMvc.perform(get("$BASE_URL/{categoryId}", existingId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(existingId))
    }

    @Test
    fun `should createCategory returns 400 when invalid data`() {
        val categoryRequest = buildProductRequest(name = "")

        mockMvc.perform(
            post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest))
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `should createCategory returns category when successful`() {
        val categoryRequest = buildCategoryRequest()

        mockMvc.perform(
            post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest))
        )
            .andExpect(header().exists("Location"))
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value(categoryRequest.name))
    }

    @Test
    fun `should updateCategory returns 404 when invalid id`() {
        val categoryRequest = buildCategoryRequest()
        val nonExistingId = 100

        mockMvc.perform(
            put("$BASE_URL/{categoryId}", nonExistingId).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should updateCategory returns updated category when successful`() {
        val categoryRequest = buildCategoryRequest("Updated Category")
        val existingId = 1

        mockMvc.perform(
            put("$BASE_URL/{categoryId}", existingId).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(existingId))
            .andExpect(jsonPath("$.name").value(categoryRequest.name))
    }

    @Test
    fun `should deleteCategory returns 404 when invalid id`() {
        val nonExistingId = 50

        mockMvc.perform(delete("$BASE_URL/{categoryId}", nonExistingId))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should deleteCategory returns 404 when category has product`() {
        val dependentId = 3

        mockMvc.perform(delete("$BASE_URL/{categoryId}", dependentId))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should deleteCategory returns 204 when successful`() {
        val successId = 5

        mockMvc.perform(delete("$BASE_URL/{categoryId}", successId))
            .andExpect(status().isNoContent)
    }

}
