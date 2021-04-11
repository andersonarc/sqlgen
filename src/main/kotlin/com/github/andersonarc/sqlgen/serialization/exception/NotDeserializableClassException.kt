package com.github.andersonarc.sqlgen.serialization.exception

class NotDeserializableClassException(clazz: Class<*>) :
    Exception("Class '${clazz.canonicalName}' could not be serialized from SQL primitive type")