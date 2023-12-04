package com.example.teentime.global.error.exception

abstract class BusinessException (
    val errorCode : ErrorCode
) : RuntimeException()