package com.example.springkotlinjwt.repository

import com.example.springkotlinjwt.entity.Role
import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, String> {
    fun findByName(name: String): Role?
}