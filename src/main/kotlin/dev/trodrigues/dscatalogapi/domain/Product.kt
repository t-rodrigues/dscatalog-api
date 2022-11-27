package dev.trodrigues.dscatalogapi.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tb_product")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String? = null,
    val date: LocalDateTime? = null,
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tb_product_category",
        joinColumns = [JoinColumn(name = "product_id")],
        inverseJoinColumns = [JoinColumn(name = "category_id")]
    )
    val categories: List<Category> = mutableListOf()
)
