package com.example.teentime.domain.user.domain.repository

import com.example.teentime.domain.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface UserRepository: JpaRepository<User, UUID>{
    fun findByAccountId(accountId: String): User
}