package com.example.teentime.domain.refreshtoken.domain

import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import org.springframework.data.redis.core.index.Indexed
import javax.persistence.Id

@RedisHash
class RefreshToken(
    @Id
    val id: String,

    @Indexed
    var token: String,

    @TimeToLive
    var ttl: Long
) {
    fun update(token: String?, ttl: Long) {
        this.token = token!!
        this.ttl = ttl
    }
}