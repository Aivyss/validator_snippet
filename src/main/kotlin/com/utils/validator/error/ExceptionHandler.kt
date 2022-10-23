package com.utils.validator.error

import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.ModelAndView

@RestControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(ValidationException::class)
    fun acceptValidationException(exception: ValidationException): ModelAndView {
        val md = ModelAndView()
        val model = md.model

        model["errorId"] = exception.errorCode

        return md
    }
}