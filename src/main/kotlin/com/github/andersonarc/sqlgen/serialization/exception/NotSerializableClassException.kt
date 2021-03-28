package com.github.andersonarc.sqlgen.serialization.exception

import java.lang.Exception

class NotSerializableClassException(clazz: Class<*>):
    Exception("Class '${clazz.canonicalName}' could not be serialized to SQL primitive type")
