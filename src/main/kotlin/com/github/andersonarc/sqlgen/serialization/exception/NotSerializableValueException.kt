package com.github.andersonarc.sqlgen.serialization.exception

import java.lang.Exception

class NotSerializableValueException(value: Any):
    Exception("Value '${value}' of type '${value.javaClass.canonicalName}' could not be serialized to SQL value")