package com.github.andersonarc.sqlgen.serialization

import com.github.andersonarc.sqlgen.serialization.reflection.ClassWrapper

private val classCache = mutableMapOf<Class<*>, ClassWrapper<*>>()

fun <T> getClassWrapper(clazz: Class<T>): ClassWrapper<T> {
    if (!classCache.contains(clazz)) {
        classCache[clazz] = ClassWrapper(clazz)
    }
    return classCache[clazz]!! as ClassWrapper<T>
}