package dev.trodrigues.dscatalogapi.repositories

import dev.trodrigues.dscatalogapi.domain.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {

//    @Query("select p from Product p join fetch p.categories")
//    fun findAllWithCategories(): List<Product>

//    @Query("select * FROM tb_product p join p.categories", nativeQuery = true)
//    fun findByIdWithCategories(id: Long): Optional<Product>

}
