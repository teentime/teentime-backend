package com.example.teentime.global.security.jwt

import com.example.teentime.domain.refreshtoken.domain.RefreshToken
import com.example.teentime.domain.refreshtoken.domain.repository.RefreshTokenRepository
import com.example.teentime.global.exception.ExpiredTokenException
import com.example.teentime.global.exception.InvalidTokenException
import com.example.teentime.global.security.principle.AuthDetailsService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import io.jsonwebtoken.*
import javax.servlet.http.HttpServletRequest

@Component
class JwtTokenProvider(
    private val jwtProperties: JwtProperties,
    private val authDetailsService: AuthDetailsService,
    private val refreshTokenRepository: RefreshTokenRepository,
) {
    companion object {
        private const val ACCESS_KEY = "access_token"
        private const val REFRESH_KEY = "refresh_token"
    }

    private fun getBody(token: String): Claims {
        return try {
            Jwts.parser().setSigningKey(jwtProperties.secretKey).parseClaimsJws(token).body
        } catch (e: JwtException) {
            throw InvalidTokenException
        }
    }

    fun getSubjectWithExpiredCheck(token: String): String {
        val body = getBody(token)
        return if (body.expiration.before(Date())) {
            throw ExpiredTokenException
        } else {
            body.subject
        }
    }

    fun reIssue(refreshToken: String): TokenResponse {
        if (!isRefreshToken(refreshToken)) {
            throw InvalidTokenException
        }

        refreshTokenRepository.findByToken(refreshToken)
            ?.let { token ->
                val id = token.id
                val role = getRole(token.token)

                val tokenResponse = generateToken(id, role)
                token.update(tokenResponse.refreshToken, jwtProperties.refreshExp)
                return TokenResponse(tokenResponse.accessToken, tokenResponse.refreshToken)
            } ?: throw InvalidTokenException
    }

    fun generateToken(userId: String, role: String): TokenResponse {
        val accessToken = generateAccessToken(userId, role, ACCESS_KEY, jwtProperties.accessExp)
        val refreshToken = generateRefreshToken(role, REFRESH_KEY, jwtProperties.refreshExp)
        refreshTokenRepository.save(
            RefreshToken(userId, refreshToken, jwtProperties.refreshExp)
        )
        return TokenResponse(accessToken, refreshToken)
    }

    private fun generateAccessToken(id: String, role: String, type: String, exp: Long): String =
        Jwts.builder()
            .setSubject(id)
            .setHeaderParam("typ", type)
            .claim("role", role)
            .signWith(SignatureAlgorithm.HS256, jwtProperties.secretKey)
            .setExpiration(Date(System.currentTimeMillis() + exp * 1000))
            .setIssuedAt(Date())
            .compact()

    private fun generateRefreshToken(role: String, type: String, exp: Long): String =
        Jwts.builder()
            .setHeaderParam("typ", type)
            .claim("role", role)
            .signWith(SignatureAlgorithm.HS256, jwtProperties.secretKey)
            .setExpiration(Date(System.currentTimeMillis() + exp * 1000))
            .setIssuedAt(Date())
            .compact()

    fun resolveToken(request: HttpServletRequest): String? =
        request.getHeader(jwtProperties.header)?.also {
            if (it.startsWith(jwtProperties.prefix)) {
                return it.substring(jwtProperties.prefix.length)
            }
        }

    fun authentication(token: String): Authentication? {
        val body: Claims = getJws(token).body
        if (!isRefreshToken(token)) throw InvalidTokenException
        val userDetails: UserDetails = getDetails(body)
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    private fun getJws(token: String): Jws<Claims> {
        return try {
            Jwts.parser().setSigningKey(jwtProperties.secretKey).parseClaimsJws(token)
        } catch (e: ExpiredJwtException) {
            throw ExpiredTokenException
        } catch (e: Exception) {
            throw InvalidTokenException
        }
    }

    private fun isRefreshToken(token: String?): Boolean {
        return REFRESH_KEY == getJws(token!!).header["typ"].toString()
    }

    fun getRole(token: String) = getJws(token).body["role"].toString()

    private fun getDetails(body: Claims): UserDetails =
        authDetailsService.loadUserByUsername(body.subject)
}
