package com.github.andersonarc.sqlgen.serialization.reflection

import com.github.andersonarc.sqlgen.serialization.insertJavaValueIntoSQL
import com.github.andersonarc.sqlgen.serialization.javaClassToSQLType
import java.lang.reflect.Field
import java.sql.PreparedStatement

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
    val name = field.name
    val sqlType = javaClassToSQLType(field.type)

    fun canGet() = getValueImpl != null

    fun canSet() = setValueImpl != null

    fun canUse() = canGet() && canSet()

    fun canPlusWith(other: FieldWrapper): Boolean {
        //todo
        return true
    }

    fun getValue(instance: Any): Any? {
        if (!canGet()) {
            throw IllegalAccessException("Field '${field.name}' value cannot be accessed")
        }
        return getValueImpl!!(instance)
    }

    fun insertIntoStatement(instance: Any, index: Int, statement: PreparedStatement) {
        insertJavaValueIntoSQL(getValue(instance), sqlType, index, statement)
    }

    fun setValue(instance: Any, value: Any?) {
        if (!canSet()) {
            throw IllegalAccessException("Field '${field.name}' could not be changed")
        }
        setValueImpl!!(instance, value)
    }
}