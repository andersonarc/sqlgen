package com.github.andersonarc.sqlgen.serialization.reflection

import com.github.andersonarc.sqlgen.misc.toTitleCase
import com.github.andersonarc.sqlgen.serialization.exception.NotDeserializableClassException
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier

typealias FieldValueProvider = ((instance: Any) -> Any?)
typealias FieldValueChanger = ((instance: Any, value: Any?) -> Any?)

fun getFieldValueProvider(field: Field): FieldValueProvider? {
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

fun getFieldValueChanger(field: Field): FieldValueChanger? {
    return if (Modifier.isFinal(field.modifiers)) {
        null
    } else if (Modifier.isPublic(field.modifiers)) {
        { instance, value ->
            field.set(instance, value)
        }
    } else {
        val setter = getFieldSetter(field)
        if (setter == null) {
            null
        } else {
            { instance, value ->
                setter.invoke(instance, value)
            }
        }
    }
}

fun getFieldSetter(field: Field): Method? {
    val setterPostfix = field.name.toTitleCase()
    for (method in field.declaringClass.declaredMethods) {
        if (method.name == "set$setterPostfix" ||
            method.name == field.name
        ) {
            if (Modifier.isPublic(method.modifiers)) {
                return method
            }
        }
    }
    return null
}

fun <T> createClassInstance(clazz: Class<T>, args: List<FieldValueWrapper>): T {
    // parse class constructors and find those which can be used
    /*val constructorFields = mutableMapOf<Constructor<*>, List<FieldValueWrapper>>()
    constructors@
    for (constructor in clazz.declaredConstructors) {
        // private constructors are not used
        if (!Modifier.isPublic(constructor.modifiers)) {
            continue
        }
        val currentFields = mutableListOf<FieldValueWrapper>()
        for (parameter in constructor.parameterTypes) {
            // check if constructor has parameters that aren't in input list
            val field = args.firstOrNull {
                parameter.isAssignableFrom(it.field.type)
            } ?: continue@constructors

            // if not, add it to the valid list
            currentFields.add(field)
        }
        constructorFields[constructor] = currentFields
    }*/

    val constructor = clazz.getConstructor()
        ?: throw NotDeserializableClassException(clazz)
    val instance = constructor.newInstance() as T
        ?: throw NotDeserializableClassException(clazz)
    for (field in args) {
        field.assignValue(instance)
    }
    return instance
}