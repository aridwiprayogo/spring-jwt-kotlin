package com.example.springjwtkotlin.dto.request

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    var employeeName: @NotBlank String = "",
    var password: @NotBlank String = ""
)
