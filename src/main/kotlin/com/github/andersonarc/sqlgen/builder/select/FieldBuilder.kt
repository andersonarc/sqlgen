package com.github.andersonarc.sqlgen.builder.select

import com.github.andersonarc.sqlgen.serialization.getClassWrapper

class FieldBuilder<T>(clazz: Class<T>) {
    private val wrapper = getClassWrapper(clazz)

    operator fun get(key: String): FieldValueBuilder {
        val field = wrapper.getFieldByName(key)
            ?: throw IllegalArgumentException("Class '${wrapper.clazz.simpleName}' does not have a field '$key'")
        return FieldValueBuilder(field)
    }
}