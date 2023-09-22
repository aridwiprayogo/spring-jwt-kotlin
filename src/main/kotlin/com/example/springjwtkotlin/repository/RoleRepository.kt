package com.example.springjwtkotlin.repository

import com.example.springjwtkotlin.entity.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, String> {
    fun findByName(name: String): Role?
}