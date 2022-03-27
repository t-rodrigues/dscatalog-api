package dev.trodrigues.dscatalogapi.resources

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import dev.trodrigues.dscatalogapi.domain.helpers.buildCategory
import dev.trodrigues.dscatalogapi.domain.helpers.buildProduct
import dev.trodrigues.dscatalogapi.domain.helpers.buildProductRequest
import dev.trodrigues.dscatalogapi.extension.toModel
import dev.trodrigues.dscatalogapi.services.ProductService
import dev.trodrigues.dscatalogapi.services.exceptions.DomainException
import dev.trodrigues.dscatalogapi.services.exceptions.ObjectNotFoundException
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.PageImpl
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [ProductResource::class])
@ContextConfiguration
class ProductResourceTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var productService: ProductService

    private val BASE_URL = "/products"

    @Test
    fun `should getProducts return an page of products`() {
        val page = PageImpl(listOf(buildProduct()))

        every { productService.findAll(any(), any()) } returns page

        mockMvc.perform(get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.items[0].id").exists())
    }

    @Test
    fun `should getProducts return an page of products with category id = 1`() {
        val categoryId = 1L
        val page = PageImpl(listOf(buildProduct(categoryId = categoryId)))

        every { productService.findAll(any(), any()) } returns page

        mockMvc.perform(get("$BASE_URL?categoryId={categoryId}", categoryId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.items[0].categories[0].id").value(categoryId))
    }

    @Test
    fun `should getProductById returns 404 when invalid id`() {
        val nonExistingId = 50L

        every { productService.findById(nonExistingId) } throws ObjectNotFoundException("")

        mockMvc.perform(get("$BASE_URL/{productId}", nonExistingId))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should getProductById returns an product response`() {
        val existingId = 1L
        val product = buildProduct()

        every { productService.findById(existingId) } returns product

        mockMvc.perform(get("$BASE_URL/{productId}", existingId))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(product.id))
    }

    @Test
    fun `should createProduct returns 404 when invalid categories`() {
        val productRequest = buildProductRequest(categories = setOf(100, 200))

        every { productService.create(any()) } throws DomainException("")

        mockMvc.perform(
            post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should createProduct returns 201 when valid data`() {
        val productRequest = buildProductRequest(categories = setOf(1, 2))
        val product = buildProduct()

        every { productService.create(any()) } returns product

        mockMvc.perform(
            post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
    }

    @Test
    fun `should updateProduct returns 404 when invalid id`() {
        val nonExistingId = 100L
        val productRequest = buildProductRequest()

        every { productService.update(nonExistingId, productRequest) } throws ObjectNotFoundException("")

        mockMvc.perform(
            put("$BASE_URL/{productId}", nonExistingId).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should updateProduct returns 400 when invalid categories`() {
        val productRequest = buildProductRequest(name = "Updated")
        val existingId = 5L

        every { productService.update(existingId, productRequest) } throws DomainException("")

        mockMvc.perform(
            put("$BASE_URL/{productId}", existingId).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should updateProduct returns 200 when valid data`() {
        val productRequest = buildProductRequest(name = "Updated", price = 120.50)
        val categories = listOf(buildCategory(id = 1), buildCategory(id = 3))
        val product = productRequest.toModel(buildProduct(), categories)
        val existingId = product.id!!

        every { productService.update(existingId, productRequest) } returns product

        mockMvc.perform(
            put("$BASE_URL/{productId}", existingId)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(productRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(product.id))
            .andExpect(jsonPath("$.name").value("Updated"))
            .andExpect(jsonPath("$.name").value(product.name))
            .andExpect(jsonPath("$.price").value(product.price))
    }

    @Test
    fun `should deleteProduct returns 404 when invalid id`() {
        val nonExistingId = 120L

        every { productService.delete(nonExistingId) } throws ObjectNotFoundException("")

        mockMvc.perform(
            delete("$BASE_URL/{productId}", nonExistingId)
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should deleteProduct returns 204 when valid id`() {
        val existingId = 5L

        every { productService.delete(existingId) } just runs

        mockMvc.perform(
            delete("$BASE_URL/{productId}", existingId)
        )
            .andExpect(status().isNoContent)
    }

}
