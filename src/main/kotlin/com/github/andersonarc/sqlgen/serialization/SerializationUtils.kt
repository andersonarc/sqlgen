package com.github.andersonarc.sqlgen.serialization

import com.github.andersonarc.sqlgen.serialization.exception.NotDeserializableClassException
import com.github.andersonarc.sqlgen.serialization.exception.NotSerializableClassException
import com.github.andersonarc.sqlgen.serialization.exception.NotSerializableValueException
import com.github.andersonarc.sqlgen.serialization.reflection.FieldWrapper
import java.lang.reflect.Field
import java.sql.JDBCType
import java.sql.PreparedStatement
import java.sql.ResultSet
import kotlin.math.abs

private const val DEFAULT_VARCHAR_LENGTH = 255
private const val DEFAULT_VARBINARY_SIZE = 255

fun javaClassToTableName(clazz: Class<*>): String {
    return clazz.simpleName + "_" + abs(clazz.name.hashCode())
}

fun javaClassToSQLType(clazz: Class<*>): JDBCType {
    return when (clazz) {
        java.lang.Byte::class.java, Byte::class.java -> JDBCType.TINYINT
        java.lang.Character::class.java, Char::class.java -> JDBCType.TINYINT
        java.lang.Short::class.java, Short::class.java -> JDBCType.SMALLINT
        java.lang.Integer::class.java, Int::class.java -> JDBCType.INTEGER
        java.lang.Long::class.java, Long::class.java -> JDBCType.BIGINT
        java.lang.Float::class.java, Float::class.java -> JDBCType.FLOAT
        java.lang.Double::class.java, Double::class.java -> JDBCType.DOUBLE
        java.lang.Boolean::class.java, Boolean::class.java -> JDBCType.BOOLEAN
        java.lang.String::class.java, String::class.java -> JDBCType.VARCHAR
        CharArray::class.java -> JDBCType.VARBINARY
        ByteArray::class.java -> JDBCType.VARBINARY
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

fun insertJavaValueIntoSQL(value: Any?, type: JDBCType, index: Int, statement: PreparedStatement) {
    if (value == null) {
        statement.setNull(index, type.vendorTypeNumber)
        return
    }
    when (value.javaClass) {
        java.lang.Byte::class.java, Byte::class.java -> statement.setByte(index, value as Byte)
        java.lang.Character::class.java, Char::class.java -> statement.setByte(index, (value as Char).toByte())
        java.lang.Short::class.java, Short::class.java -> statement.setShort(index, value as Short)
        java.lang.Integer::class.java, Int::class.java -> statement.setInt(index, value as Int)
        java.lang.Long::class.java, Long::class.java -> statement.setLong(index, value as Long)
        java.lang.Float::class.java, Float::class.java -> statement.setFloat(index, value as Float)
        java.lang.Double::class.java, Double::class.java -> statement.setDouble(index, value as Double)
        java.lang.Boolean::class.java, Boolean::class.java -> statement.setBoolean(index, value as Boolean)
        java.lang.String::class.java, String::class.java -> statement.setString(index, value as String)
        ByteArray::class.java -> statement.setBytes(index, value as ByteArray)
        CharArray::class.java -> statement.setBytes(index, (value as CharArray).map(Char::toByte).toByteArray())
        else -> throw NotSerializableValueException(value)
    }
}

fun getSerializableFields(clazz: Class<*>): List<FieldWrapper> {
    return clazz.declaredFields
        .map { FieldWrapper(it) }
        .filter(FieldWrapper::canUse)
        .sortedBy { it.field.name }
}