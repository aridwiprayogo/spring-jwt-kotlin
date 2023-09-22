package com.example.springjwtkotlin.security.services

import com.example.springjwtkotlin.entity.Employee
import com.example.springjwtkotlin.entity.Role
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors

class EmployeeDetailsImpl(
    val id: String?,
    private val username: String,
    val email: String,
    @field:JsonIgnore private val password: String,
    private val authorities: Collection<GrantedAuthority>
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities as MutableCollection<out GrantedAuthority>
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val user = other as EmployeeDetailsImpl
        return id == user.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    companion object {
        private const val serialVersionUID = 1L

        fun build(user: Employee): EmployeeDetailsImpl {
            val authorities = user.role.stream()
                .map { role: Role? ->
                    SimpleGrantedAuthority(role!!.name)
                }.collect(Collectors.toList())
            return EmployeeDetailsImpl(
                user.id,
                user.name,
                user.email,
                user.password,
                authorities
            )
        }
    }
}