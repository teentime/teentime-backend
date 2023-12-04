package com.example.teentime.global.exception

import com.example.teentime.global.error.exception.BusinessException
import com.example.teentime.global.error.exception.ErrorCode

object InvalidTokenException: BusinessException (
    ErrorCode.INVALID_TOKEN
)