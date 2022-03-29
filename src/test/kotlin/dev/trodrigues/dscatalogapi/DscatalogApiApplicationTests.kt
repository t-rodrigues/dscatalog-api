package dev.trodrigues.dscatalogapi

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class DscatalogApiApplicationTests {

    @Test
    fun contextLoads() {
    }

}
