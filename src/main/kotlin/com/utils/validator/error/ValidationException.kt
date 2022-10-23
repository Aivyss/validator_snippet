package com.utils.validator.error

class ValidationException (val errorCode: String = "DEFAULT_ERROR"): RuntimeException()