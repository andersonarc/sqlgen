package com.github.andersonarc.sqlgen.serialization.reflection

import com.github.andersonarc.sqlgen.serialization.getSerializableFields
import com.github.andersonarc.sqlgen.serialization.javaClassToTableName

class ClassWrapper<T>(val clazz: Class<T>) {
    val fields = getSerializableFields(clazz)
    val tableName = javaClassToTableName(clazz)

    fun getFieldByName(name: String): FieldWrapper? {
        return fields.find { it.field.name == name }
    }

    fun createInstance(args: List<FieldValueWrapper>): T {
        return createClassInstance(clazz, args)
    }
}