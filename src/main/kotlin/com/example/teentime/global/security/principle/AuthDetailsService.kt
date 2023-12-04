package com.example.teentime.global.security.principle

import com.example.teentime.domain.user.domain.User
import com.example.teentime.domain.user.facade.UserFacade
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AuthDetailsService(
    private val userFacade: UserFacade
): UserDetailsService {
    override fun loadUserByUsername(accountId: String): UserDetails {
        val user: User = accountId.let { userFacade.findByAccountId(it) }
        return AuthDetails(user.email)
    }
}