package com.github.andersonarc.sqlgen.serialization.reflection

import java.lang.reflect.Field

class CachedField(val field: Field) {
    private val getValueImpl: ((Any) -> Any)? = getFieldValueProvider(field)

    fun isAccessible() = getValueImpl != null

    fun getValue(instance: Any) = getValueImpl!!(instance)
}