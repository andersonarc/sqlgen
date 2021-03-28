package com.github.andersonarc.sqlgen.serialization

import com.github.andersonarc.sqlgen.serialization.exception.NotSerializableClassException
import com.github.andersonarc.sqlgen.serialization.exception.NotSerializableValueException
import com.github.andersonarc.sqlgen.serialization.reflection.CachedField
import kotlin.math.abs

object SQLSerializer {
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

    fun javaValueToSQLValue(value: Any): String {
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

    fun getSerializableFields(clazz: Class<*>): List<CachedField> {
        return clazz.declaredFields
            .map { CachedField(it) }
            .filter(CachedField::isAccessible)
            .sortedBy { it.field.name }
    }
}