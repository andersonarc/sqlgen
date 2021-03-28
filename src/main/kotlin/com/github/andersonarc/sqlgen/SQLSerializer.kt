package com.github.andersonarc.sqlgen

import com.github.andersonarc.sqlgen.serialization.exception.NotSerializableClassException
import com.github.andersonarc.sqlgen.serialization.exception.NotSerializableValueException

object SQLSerializer {
    private const val DEFAULT_VARCHAR_LENGTH = 255
    private const val DEFAULT_VARBINARY_SIZE = 255

    fun javaClassToTableName(clazz: Class<*>): String {
        return clazz.simpleName
    }

    fun javaClassToSQLType(clazz: Class<*>): String {
        return when (clazz) {
            Byte::class.java -> "TINYINT"
            Char::class.java -> "TINYINT"
            Short::class.java -> "SMALLINT"
            Int::class.java -> "INTEGER"
            Long::class.java -> "BIGINT"
            Float::class.java -> "FLOAT"
            Double::class.java -> "DOUBLE"
            Boolean::class.java -> "BOOL"
            String::class.java -> "VARCHAR($DEFAULT_VARCHAR_LENGTH)"
            CharArray::class.java -> "VARBINARY($DEFAULT_VARBINARY_SIZE)"
            ByteArray::class.java -> "VARBINARY($DEFAULT_VARBINARY_SIZE)"
            else -> throw NotSerializableClassException(clazz)
        }
    }

    fun javaValueToSQLValue(value: Any): String {
        return when (value.javaClass) {
            Byte::class.java -> value.toString()
            Char::class.java -> value.toString()
            Short::class.java -> value.toString()
            Int::class.java -> value.toString()
            Long::class.java -> value.toString()
            Float::class.java -> value.toString()
            Double::class.java -> value.toString()
            Boolean::class.java -> value.toString().toUpperCase()
            String::class.java -> value.toString()
            CharArray::class.java -> value.toString()
            ByteArray::class.java -> value.toString()
            else -> throw NotSerializableValueException(value)
        }
    }
}