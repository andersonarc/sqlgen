package com.github.andersonarc.sqlgen.serialization.reflection

import com.github.andersonarc.sqlgen.misc.toTitleCase
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier

fun getFieldValueProvider(field: Field): ((Any) -> Any)? {
    return if (Modifier.isPublic(field.modifiers)) {
        {
            field.get(it)
        }
    } else {
        val getter = getFieldGetter(field)
        if (getter == null) {
            null
        } else {
            {
                getter.invoke(it)
            }
        }
    }
}

fun getFieldGetter(field: Field): Method? {
    val getterPostfix = field.name.toTitleCase()
    for (method in field.declaringClass.declaredMethods) {
        if (method.name == "get$getterPostfix" ||
            method.name == "is$getterPostfix" ||
            method.name == field.name
        ) {
            if (Modifier.isPublic(method.modifiers)) {
                return method
            }
        }
    }
    return null
}