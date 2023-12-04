package com.example.teentime.global.exception

import com.example.teentime.global.error.exception.BusinessException
import com.example.teentime.global.error.exception.ErrorCode

object ExpiredTokenException: BusinessException (
    ErrorCode.EXPIRED_TOKEN
)