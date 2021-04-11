package com.github.andersonarc.sqlgen.serialization

import com.github.andersonarc.sqlgen.serialization.exception.NotDeserializableClassException
import com.github.andersonarc.sqlgen.serialization.exception.NotSerializableClassException
import com.github.andersonarc.sqlgen.serialization.exception.NotSerializableValueException
import com.github.andersonarc.sqlgen.serialization.reflection.FieldWrapper
import java.lang.reflect.Field
import java.sql.ResultSet
import kotlin.math.abs

private const val DEFAULT_VARCHAR_LENGTH = 255
private const val DEFAULT_VARBINARY_SIZE = 255

fun javaClassToTableName(clazz: Class<*>): String {
    return clazz.simpleName + "_" + abs(clazz.name.hashCode())
}

fun javaClassToSQLType(clazz: Class<*>): String {
    return when (clazz) {
        java.lang.Byte::class.java, Byte::class.java -> "TINYINT"
        java.lang.Character::class.java, Char::class.java -> "TINYINT"
        java.lang.Short::class.java, Short::class.java -> "SMALLINT"
        java.lang.Integer::class.java, Int::class.java -> "INTEGER"
        java.lang.Long::class.java, Long::class.java -> "BIGINT"
        java.lang.Float::class.java, Float::class.java -> "FLOAT"
        java.lang.Double::class.java, Double::class.java -> "DOUBLE"
        java.lang.Boolean::class.java, Boolean::class.java -> "BOOL"
        java.lang.String::class.java, String::class.java -> "VARCHAR($DEFAULT_VARCHAR_LENGTH)"
        CharArray::class.java -> "VARBINARY($DEFAULT_VARBINARY_SIZE)"
        ByteArray::class.java -> "VARBINARY($DEFAULT_VARBINARY_SIZE)"
        else -> throw NotSerializableClassException(clazz)
    }
}

fun sqlValueToJavaValue(field: Field, result: ResultSet): Any? {
    val value: Any? = when (field.type) {
        java.lang.Byte::class.java, Byte::class.java -> result.getByte(field.name)
        java.lang.Character::class.java, Char::class.java -> result.getByte(field.name).toChar()
        java.lang.Short::class.java, Short::class.java -> result.getShort(field.name)
        java.lang.Integer::class.java, Int::class.java -> result.getInt(field.name)
        java.lang.Long::class.java, Long::class.java -> result.getLong(field.name)
        java.lang.Float::class.java, Float::class.java -> result.getFloat(field.name)
        java.lang.Double::class.java, Double::class.java -> result.getDouble(field.name)
        java.lang.Boolean::class.java, Boolean::class.java -> result.getBoolean(field.name)
        java.lang.String::class.java, String::class.java -> result.getString(field.name)
        CharArray::class.java -> result.getArray(field.name).array as CharArray?
        ByteArray::class.java -> result.getArray(field.name).array as ByteArray?
        else -> throw NotDeserializableClassException(field.type)
    }
    return if (result.wasNull()) {
        null
    } else {
        value
    }
}

fun javaValueToSQLValue(value: Any?): String {
    if (value == null) {
        return "NULL"
    }
    return when (value.javaClass) {
        java.lang.Byte::class.java, Byte::class.java,
        java.lang.Character::class.java, Char::class.java,
        java.lang.Short::class.java, Short::class.java,
        java.lang.Integer::class.java, Int::class.java,
        java.lang.Long::class.java, Long::class.java,
        java.lang.Float::class.java, Float::class.java,
        java.lang.Double::class.java, Double::class.java,
        java.lang.String::class.java, String::class.java,
        ByteArray::class.java,
        CharArray::class.java
        -> value.toString()

        java.lang.Boolean::class.java, Boolean::class.java -> value.toString().toUpperCase()
        else -> throw NotSerializableValueException(value)
    }
}

fun getSerializableFields(clazz: Class<*>): List<FieldWrapper> {
    return clazz.declaredFields
        .map { FieldWrapper(it) }
        .filter(FieldWrapper::canGet)
        .sortedBy { it.field.name }
}