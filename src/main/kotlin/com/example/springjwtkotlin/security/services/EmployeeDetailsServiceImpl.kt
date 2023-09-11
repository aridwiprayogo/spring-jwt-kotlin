package com.example.springkotlinjwt.security.services

import com.example.springkotlinjwt.entity.Employee
import com.example.springkotlinjwt.repository.EmployeeRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class EmployeeDetailsServiceImpl : UserDetailsService {
    @Autowired
    lateinit var employeeRepository: EmployeeRepository

    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String?): UserDetails {
        val employee: Employee =
            employeeRepository.findByName(username!!) ?: throw UsernameNotFoundException("User not found")
        return EmployeeDetailsImpl.build(employee)
    }
}