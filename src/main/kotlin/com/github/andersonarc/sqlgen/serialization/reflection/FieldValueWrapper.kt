package com.github.andersonarc.sqlgen.serialization.reflection

import java.lang.reflect.Field

/**
 * A [FieldWrapper], that stores a value that should be written to a field.
 */
class FieldValueWrapper(field: Field, val value: Any?) : FieldWrapper(field) {
    fun assignValue(instance: Any) {
        setValue(instance, value)
    }
}