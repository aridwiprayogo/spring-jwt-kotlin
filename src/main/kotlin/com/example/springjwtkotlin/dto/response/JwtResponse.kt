package com.example.springkotlinjwt.dto.response

data class JwtResponse(
    val accessToken: String, var id: String,
    var employeename: String, var email: String, val roles: List<String>
) {
    var tokenType = "Bearer"
}

