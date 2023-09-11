package com.example.springkotlinjwt.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class Role(var name: String? = null) {
    @Id
    var id: String? = null
}
