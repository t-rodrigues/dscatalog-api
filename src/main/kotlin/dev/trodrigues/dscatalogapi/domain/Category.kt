package dev.trodrigues.dscatalogapi.domain

import jakarta.persistence.*
import java.time.LocalDateTime
import java.time.ZoneId

@Entity
@Table(name = "tb_category")
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(length = 100)
    val name: String,
    val createdAt: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC")),
    val updatedAt: LocalDateTime? = null,
    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    val products: Set<Product> = mutableSetOf()
)
