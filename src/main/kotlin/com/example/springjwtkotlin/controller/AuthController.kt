package com.example.springkotlinjwt.controller

import com.example.springkotlinjwt.dto.request.LoginRequest
import com.example.springkotlinjwt.dto.request.SignUpRequest
import com.example.springkotlinjwt.dto.response.JwtResponse
import com.example.springkotlinjwt.dto.response.MessageResponse
import com.example.springkotlinjwt.entity.ERole
import com.example.springkotlinjwt.entity.Employee
import com.example.springkotlinjwt.entity.Role
import com.example.springkotlinjwt.repository.EmployeeRepository
import com.example.springkotlinjwt.repository.RoleRepository
import com.example.springkotlinjwt.security.jwt.JwtUtils
import com.example.springkotlinjwt.security.services.EmployeeDetailsImpl
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController {
    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var employeeRepository: EmployeeRepository

    @Autowired
    lateinit var roleRepository: RoleRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var jwtUtils: JwtUtils

    @PostMapping("/signin")
    fun login(@RequestBody loginRequest: @Valid LoginRequest): ResponseEntity<*> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequest.employeeName,
                loginRequest.password
            )
        )
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtUtils.generateJwtToken(authentication)
        val employeeDetails = authentication.principal as EmployeeDetailsImpl
        val roles = employeeDetails.authorities.map { grantedAuthority -> grantedAuthority.authority }.toList()

        return ResponseEntity.ok(
            JwtResponse(
                accessToken = jwt,
                id = employeeDetails.id!!,
                employeename = employeeDetails.username,
                email = employeeDetails.email,
                roles = roles,
            )
        )
    }

    @PostMapping("/signup")
    fun signUp(
        @RequestBody request:
        @Valid SignUpRequest
    ): ResponseEntity<*> {
        if (employeeRepository.existsByName(request.employeeName)!!) {
            return ResponseEntity.badRequest().body("Employee already exists")
        }
        if (employeeRepository.existsByEmail(request.email)!!) {
            return ResponseEntity.badRequest().body("Email already exists")
        }

        val employee = Employee(
            request.employeeName,
            request.email,
            passwordEncoder.encode(request.password)
        )
        val strRoles = request.roles
        val roles: MutableSet<Role> = HashSet()
        if (strRoles.isEmpty()) {
            val employeeRole = roleRepository.findByName(ERole.ROLE_EMPLOYEE.name)
                ?: throw RuntimeException("Error: Role is not found.")
            roles.add(employeeRole)
        } else {
            for (role in strRoles) {
                val employeeRole = roleRepository.findByName(role)
                    ?: throw RuntimeException("Error: Role is not found.")
                roles.add(employeeRole)
            }
        }
        employee.role = roles
        employeeRepository.save(employee)
        return ResponseEntity.ok().body(MessageResponse("Employee registered successfully!"))
    }

}