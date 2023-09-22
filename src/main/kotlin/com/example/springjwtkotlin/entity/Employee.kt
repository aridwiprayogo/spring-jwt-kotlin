package com.example.springjwtkotlin.entity

import jakarta.persistence.*
import jakarta.validation.constraints.*

@Entity
@Table(name = "employees", schema = "users")
data class Employee(
    @Id
    var id: String = "",
    @NotBlank
    @Size(min = 3, max = 20)
    var name: String = "",
    @NotBlank
    @Size(min = 3, max = 50)
    @Email
    var email: String = "",
    @NotBlank
    @Size(min = 3, max = 20)
    var password: String = "",

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeRole")
    var role: Set<Role> = mutableSetOf(),
)
