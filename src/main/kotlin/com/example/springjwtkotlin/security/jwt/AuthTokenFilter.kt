package com.example.springkotlinjwt.security.jwt

import com.example.springkotlinjwt.security.services.EmployeeDetailsServiceImpl
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter

@Component
class AuthTokenFilter : OncePerRequestFilter() {
    @Autowired
    private lateinit var jwtUtils: JwtUtils

    @Autowired
    private lateinit var employeeService: EmployeeDetailsServiceImpl

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val headerAuth: String? = getHeaderAuth(request)
            val jwt = parseJwt(headerAuth)
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                val authentication =
                    usernamePasswordAuthentication(jwt)
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: Exception) {
            logger.error("Cannot set employee authentication: $e", e)
        }

        filterChain.doFilter(request, response)
    }

    fun usernamePasswordAuthentication(jwt: String?): UsernamePasswordAuthenticationToken {
        val employeeName = jwtUtils.getEmployeeNameFrom(jwt.toString())
        val employeeDetails = employeeService.loadUserByUsername(employeeName)
        return UsernamePasswordAuthenticationToken(
            employeeDetails,
            null,
            employeeDetails.authorities
        )
    }

    private fun getHeaderAuth(request: HttpServletRequest) =
        request.getHeader("Authorization")

    private fun parseJwt(headerAuth: String?) = if (StringUtils.hasText(headerAuth) &&
        headerAuth?.startsWith("Bearer")!!) {
            headerAuth.substring(7, headerAuth.length)
        } else null

    companion object {
        private val logger = LoggerFactory.getLogger(AuthTokenFilter::class.java)
    }
}