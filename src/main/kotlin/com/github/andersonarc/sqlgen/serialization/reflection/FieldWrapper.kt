package com.github.andersonarc.sqlgen.serialization.reflection

import java.lang.reflect.Field

/**
 * Finds methods to set/get field value, using:
 * 1) direct field access
 * 2) field getter or setter
 *
 * These methods are stored as class fields and called when required.
 */
open class FieldWrapper(val field: Field) {
    private val getValueImpl: FieldValueProvider? = getFieldValueProvider(field)
    private val setValueImpl: FieldValueChanger? = getFieldValueChanger(field)

    fun canGet() = getValueImpl != null

    fun canSet() = setValueImpl != null

    fun canUse() = canGet() && canSet()

    fun getValue(instance: Any): Any? {
        if (!canGet()) {
            throw IllegalAccessException("Field '${field.name}' value cannot be accessed")
        }
        return getValueImpl!!(instance)
    }

    fun setValue(instance: Any, value: Any?) {
        if (!canSet()) {
            throw IllegalAccessException("Field '${field.name}' could not be changed")
        }
        setValueImpl!!(instance, value)
    }
}