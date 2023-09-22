package com.example.springjwtkotlin.controller

import com.example.springjwtkotlin.dto.response.MessageResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/test")
class EmployeeController {
    @GetMapping("/all")
    fun getAll(): MessageResponse {
        return MessageResponse("Public")
    }

    @GetMapping("/employee")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    fun getEmployee(): MessageResponse {
        return MessageResponse("Employ")
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun getAdmin(): MessageResponse {
        return MessageResponse("Admin")
    }
}