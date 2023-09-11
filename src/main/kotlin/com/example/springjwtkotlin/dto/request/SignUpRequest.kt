package com.example.springkotlinjwt.dto.request

import java.util.HashSet

data class SignUpRequest(
    var employeeName  : String      = "",
    var email         : String      = "",
    var password      : String      = "",
    var roles         : Set<String> = HashSet()
)