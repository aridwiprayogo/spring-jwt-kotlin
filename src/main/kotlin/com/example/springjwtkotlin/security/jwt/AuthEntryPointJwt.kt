package com.example.springkotlinjwt.security.jwt

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class AuthEntryPointJwt : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        logger.error("Unauthorized error: {}", authException?.message)
        response?.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException?.message)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AuthEntryPointJwt::class.java)
    }
}