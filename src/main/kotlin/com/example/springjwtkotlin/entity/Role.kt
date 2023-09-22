package com.example.springjwtkotlin.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "role", schema = "users")
class Role(var name: String? = null) {
    @Id
    var id: String? = null
}
