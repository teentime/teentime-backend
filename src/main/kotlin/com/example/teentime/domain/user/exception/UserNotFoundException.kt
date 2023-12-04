package com.example.teentime.domain.user.exception

import com.example.teentime.global.error.exception.BusinessException
import com.example.teentime.global.error.exception.ErrorCode

object UserNotFoundException: BusinessException(
    ErrorCode.USER_NOT_FOUND
)