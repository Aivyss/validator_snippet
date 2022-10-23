package com.utils.validator.validators

import com.utils.validator.error.ErrorGenerator
import com.utils.validator.error.ValidationException

interface CustomValidator<T> {
    fun isSupported(any: Any): Boolean
    fun isValid(any: Any, errorGenerator: ErrorGenerator): Boolean
    fun defaultException(): ValidationException
}