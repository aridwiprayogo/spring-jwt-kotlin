package com.example.springjwtkotlin.repository

import com.example.springjwtkotlin.entity.Employee
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeeRepository : JpaRepository<Employee, String> {
    fun findByName(name: String): Employee?
    fun existsByName(name: String): Boolean?
    fun existsByEmail(email: String): Boolean?
}
