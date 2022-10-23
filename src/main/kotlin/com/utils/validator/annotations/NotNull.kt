package com.utils.validator.annotations

@SimpleValidationAnnotation
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class NotNull(
    val errorId: String
)