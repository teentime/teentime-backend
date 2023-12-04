package com.example.teentime.domain.user.facade

import com.example.teentime.domain.user.domain.User
import com.example.teentime.domain.user.domain.repository.UserRepository
import com.example.teentime.domain.user.exception.UserNotFoundException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class UserFacade (
    private val userRepository: UserRepository
){
    fun currentUser(): User {
        val accountId = SecurityContextHolder.getContext().authentication.name
        return findByAccountId(accountId)
    }

    fun findByAccountId(accountId: String): User =
        userRepository.findByAccountId(accountId) ?: throw UserNotFoundException
}
