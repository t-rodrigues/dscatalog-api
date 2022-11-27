package dev.trodrigues.dscatalogapi.integration

import com.fasterxml.jackson.databind.ObjectMapper
import dev.trodrigues.dscatalogapi.domain.helpers.buildProductRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@SpringBootTest
@AutoConfigureMockMvc
internal class ProductResourceIT {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        const val BASE_URL = "/products"
    }

    @Test
    fun `should getProducts return a page of products with category id when id is provided`() {
        val categoryId = 1

        mockMvc.get(BASE_URL) {
            param("categoryId", "$categoryId")
        }.andExpect {
            status { isOk() }
            jsonPath("$.items[*].id").value(categoryId)
        }
    }

    @Test
    fun `should getProducts return a page of products`() {
        mockMvc.get(BASE_URL).andExpect {
            status { isOk() }
            jsonPath("$.items[*].id").exists()
        }
    }

    @Test
    fun `should getProductById returns 404 when invalid id`() {
        val nonExistingId = 120

        mockMvc.get("$BASE_URL/{productId}", nonExistingId).andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `should getProductById return a product when id exists`() {
        val existingId = 1

        mockMvc.get("$BASE_URL/{productId}", existingId).andExpect {
            status { isOk() }
            jsonPath("$.id").value(existingId)
        }
    }

    @Test
    fun `should createProduct returns 400 when invalid data`() {
        val productRequest = buildProductRequest(name = "")

        mockMvc.post(BASE_URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(productRequest)
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `should createProduct returns 400 when invalid categories`() {
        val productRequest = buildProductRequest(categories = setOf(50, 60))

        mockMvc.post(BASE_URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(productRequest)
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `should createProduct return a Product when valid data`() {
        val productRequest = buildProductRequest()

        mockMvc.post(BASE_URL) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(productRequest)
        }.andExpect {
            status { isCreated() }
            header { exists("Location") }
            jsonPath("$.id").exists()
            jsonPath("$.name").value(productRequest.name)
            jsonPath("$.description").value(productRequest.description)
            jsonPath("$.price").value(productRequest.price)
        }
    }

    @Test
    fun `should updateProduct returns 404 when invalid id`() {
        val nonExistingId = 100
        val productRequest = buildProductRequest()

        mockMvc.put("$BASE_URL/{productId}", nonExistingId) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(productRequest)
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `should updateProduct returns 400 when invalid data`() {
        val productRequest = buildProductRequest(name = "")
        val existingId = 5

        mockMvc.put("$BASE_URL/{productId}", existingId) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(productRequest)
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `should updateProduct returns 400 when invalid categories`() {
        val productRequest = buildProductRequest(categories = setOf(10, 30))
        val existingId = 5

        mockMvc.put("$BASE_URL/{productId}", existingId) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(productRequest)
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `should updateProduct return updated product`() {
        val productRequest = buildProductRequest(name = "Updated", price = 10.20, description = "Lorem ipsum")
        val existingId = 5

        mockMvc.put("$BASE_URL/{productId}", existingId) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(productRequest)
        }.andExpect {
            status { isOk() }
            jsonPath("$.id").value(existingId)
            jsonPath("$.name").value(productRequest.name)
            jsonPath("$.description").value(productRequest.description)
            jsonPath("$.price").value(productRequest.price)
        }
    }

    @Test
    fun `should deleteProduct returns 404 when invalid id`() {
        val nonExistingId = 50

        mockMvc.delete("$BASE_URL/{productId}", nonExistingId).andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `should deleteProduct returns nothing when successful`() {
        val existingId = 10

        mockMvc.delete("$BASE_URL/{productId}", existingId).andExpect {
            status { isNoContent() }
        }
    }

}
