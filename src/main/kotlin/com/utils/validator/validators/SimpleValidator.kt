package com.utils.validator.validators

import com.utils.validator.annotations.HandlerConstraint
import com.utils.validator.annotations.NotEmptyList
import com.utils.validator.annotations.NotNull
import org.springframework.stereotype.Component

@Component
class SimpleValidator {
    @HandlerConstraint(NotNull::class)
    fun checkNotNullConstraint(data: Any): Boolean = data != null

    @HandlerConstraint(NotEmptyList::class)
    fun checkNotEmptyListConstraint(data: Any): Boolean = data != null && data::class == List::class && (data as List<*>).isEmpty()
}