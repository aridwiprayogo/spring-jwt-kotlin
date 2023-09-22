package com.example.springjwtkotlin.security

import com.example.springjwtkotlin.security.jwt.AuthEntryPointJwt
import com.example.springjwtkotlin.security.jwt.AuthTokenFilter
import com.example.springjwtkotlin.security.services.EmployeeDetailsServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


typealias AuthorizationManagerRequestMatcherRegistryCustomizer = Customizer<AuthorizationManagerRequestMatcherRegistry?>

typealias AuthorizationManagerRequestMatcherRegistry = AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class WebSecurityConfig(
    var authTokenFilter: AuthTokenFilter,
    var unauthorizedHandler: AuthEntryPointJwt,
    var employeeDetailsService: EmployeeDetailsServiceImpl
) {

    @Suppress("ObjectLiteralToLambda")
    @Throws(Exception::class)
    @Bean
    fun configure(http: HttpSecurity): SecurityFilterChain {
        http.cors(/* corsCustomizer = */ object : Customizer<CorsConfigurer<HttpSecurity>?> {
            override fun customize(config: CorsConfigurer<HttpSecurity>?) {
                config?.configurationSource(UrlBasedCorsConfigurationSource().also { cors ->
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
            }
        })
        http.csrf().disable()
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.securityMatcher("/api/**")
            .authorizeHttpRequests(object : AuthorizationManagerRequestMatcherRegistryCustomizer {
                override fun customize(authorizeRequests: AuthorizationManagerRequestMatcherRegistry?) {
                    authorizeRequests?.requestMatchers("/api/auth/**")?.permitAll()
                    authorizeRequests?.requestMatchers("/api/test/**")?.permitAll()
                    authorizeRequests?.anyRequest()?.permitAll()
                }
            })
        http.httpBasic().disable()
        http.formLogin().disable()
        http.exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
        http.addFilterBefore(
            authTokenFilter, UsernamePasswordAuthenticationFilter::class.java
        ).authenticationProvider(authenticationProvider())
        return http.build()
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
