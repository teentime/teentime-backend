package com.example.teentime.global.security.jwt

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)
