package com.utils.validator.control

import com.utils.validator.annotations.HandlerConstraint
import com.utils.validator.annotations.Valid
import com.utils.validator.error.ErrorGenerator
import com.utils.validator.error.ValidationException
import com.utils.validator.validators.CustomValidator
import com.utils.validator.validators.SimpleValidator
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@Aspect
@Order(10)
@Component
class ValidationAspect @Autowired constructor(
    private val simpleValidator: SimpleValidator,
    applicationContext: ApplicationContext,
) {
    private val handlerConstraintMap: Map<KClass<*>, KFunction<*>>
    private val validators: Map<String, CustomValidator<*>>

    init {
        validators = applicationContext.getBeansOfType(CustomValidator::class.java)

        handlerConstraintMap = SimpleValidator::class.functions.stream().map {
            it.findAnnotation<HandlerConstraint>()?.value to it
        }.filter { it?.first != null }.toList().associate { e -> e as Pair<KClass<*>, KFunction<*>> }
    }

    @Around("@annotation(org.springframework.stereotype.Controller)")
    fun checkValidation(point: ProceedingJoinPoint): Any {
        val locale = LocaleContextHolder.getLocale()
        val signature = point.signature as MethodSignature
        val parameters = signature.method.parameters
        val arguments = point.args

        parameters.forEachIndexed { idx, param ->
            val argument = arguments[idx]

            if (param.getAnnotation(Valid::class.java) != null) {
                val kClass = argument::class
                val memberProperties = kClass.declaredMemberProperties

                memberProperties.forEach { memberProperty ->
                    memberProperty.isAccessible = true
                    val memberValue = memberProperty.getter.call(argument)
                    val memberPropertyAnnotations = memberProperty.annotations

                    memberPropertyAnnotations.forEach { memberPropertyAnnotation ->
                        val memberAnnotationType = memberProperty::class
                        val validationFunction = handlerConstraintMap[memberAnnotationType]

                        if (validationFunction != null) {
                            validationFunction.isAccessible = true

                            val isValid = validationFunction.call(simpleValidator, memberValue) as Boolean

                            if (!isValid) {
                                val errorCode = getCode<String>("errorId", memberPropertyAnnotation)

                                throw ValidationException(errorCode ?: "DEFAULT_ERROR")
                            }
                        }
                    }
                }
            }

            validators.values.forEach { validator ->
                val eg = ErrorGenerator()

                if (validator.isSupported(param) && !validator.isValid(argument, eg)) {
                    throw if (eg.isDefault) ValidationException() else   ValidationException(eg.errorId)
                }
            }
        }



        return point.proceed()
    }

    private fun <T> getCode(memberName: String, validationAnnotation: Annotation): T? {
        return validationAnnotation::class.memberProperties.map {
            if (it.name == memberName) {
                return it.getter.call(validationAnnotation) as T
            } else {
                null
            }
        }.filterNotNull().first()
    }
}