package dev.trodrigues.dscatalogapi.resources

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import dev.trodrigues.dscatalogapi.domain.helpers.buildCategory
import dev.trodrigues.dscatalogapi.services.CategoryService
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.PageImpl
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

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
    fun `should return an page of categories`() {
        val page = PageImpl(listOf(buildCategory()))

        every { categoryService.findAll(any()) } returns page

        mockMvc.perform(get(BASE_URL))
            .andExpect(jsonPath("$.items[0].id").exists())
    }

}
