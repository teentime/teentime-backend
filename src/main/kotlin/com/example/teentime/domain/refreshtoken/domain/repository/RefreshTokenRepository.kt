package com.example.teentime.domain.refreshtoken.domain.repository

import com.example.teentime.domain.refreshtoken.domain.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository: JpaRepository<RefreshToken, String> {
    fun findByToken(token: String): RefreshToken?
}