package com.utils.validator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy

@SpringBootApplication
@EnableAspectJAutoProxy
class ValidatorApplication

fun main(args: Array<String>) {
    runApplication<ValidatorApplication>(*args)
}
