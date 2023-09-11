package com.example.springkotlinjwt.controller

import com.example.springkotlinjwt.dto.response.MessageResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.CrossOrigin
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
    @PreAuthorize("hasRole('EMPLOYEE')")
    fun getEmployee(): MessageResponse {
        return MessageResponse("Employ")
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    fun getAdmin(): MessageResponse {
        return MessageResponse("Admin")
    }
}