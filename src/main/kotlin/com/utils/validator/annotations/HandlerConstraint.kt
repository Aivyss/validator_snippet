package com.utils.validator.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class HandlerConstraint(
    val value: KClass<*>
)
