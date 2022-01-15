package dev.trodrigues.dscatalogapi.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "tb_product")
data class Product(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String? = null,
    val date: LocalDateTime,

    @ManyToMany
    @JoinTable(
        name = "tb_product_category",
        joinColumns = [JoinColumn(name = "product_id")],
        inverseJoinColumns = [JoinColumn(name = "category_id")]
    )
    @JsonIgnoreProperties("products")
    val categories: List<Category> = mutableListOf()

)
