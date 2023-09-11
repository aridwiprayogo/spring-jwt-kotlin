package com.example.springkotlinjwt.security

import com.example.springkotlinjwt.security.jwt.AuthEntryPointJwt
import com.example.springkotlinjwt.security.jwt.AuthTokenFilter
import com.example.springkotlinjwt.security.services.EmployeeDetailsServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
    securedEnabled = true,
    prePostEnabled = true,
    jsr250Enabled = true,
)
class WebSecurityConfig(
    var authTokenFilter: AuthTokenFilter,
    var unauthorizedHandler: AuthEntryPointJwt,
    var employeeDetailsService: EmployeeDetailsServiceImpl
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain?, HttpSecurity>() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.cors { config ->
            config.configurationSource(UrlBasedCorsConfigurationSource().also { cors ->
                CorsConfiguration().apply {
                    allowedOrigins = listOf("*")
                    allowedMethods = listOf("POST", "PUT", "DELETE", "GET", "OPTIONS", "HEAD")
                    allowedHeaders = listOf(
                        "Authorization",
                        "Content-Type",
                        "X-Requested-With",
                        "Accept",
                        "Origin",
                        "Access-Control-Request-Method",
                        "Access-Control-Request-Headers"
                    )
                    exposedHeaders = listOf(
                        "Access-Control-Allow-Origin",
                        "Access-Control-Allow-Credentials",
                        "Authorization",
                        "Content-Disposition"
                    )
                    maxAge = 3600
                    cors.registerCorsConfiguration("/**", this)
                }
            })
        }.csrf { csrf -> csrf.disable() }.sessionManagement { sessionManagement ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
            .securityMatcher("/api/**")
            .authorizeHttpRequests { authorizeRequests ->
                authorizeRequests.requestMatchers(
                    "/api/auth/**",
                    "/api/test/**",
                ).permitAll()
            }.httpBasic(Customizer.withDefaults())
            .formLogin(Customizer.withDefaults())
            .exceptionHandling { exceptionHandler ->
                exceptionHandler.authenticationEntryPoint(unauthorizedHandler)
            }.addFilterBefore(
                authTokenFilter, UsernamePasswordAuthenticationFilter::class.java
            ).authenticationProvider(authenticationProvider())
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authenticationProvider() = DaoAuthenticationProvider()
        .apply {
            setUserDetailsService(employeeDetailsService)
            setPasswordEncoder(passwordEncoder())
        }

    @Bean
    @Throws(java.lang.Exception::class)
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.getAuthenticationManager()
    }


}
