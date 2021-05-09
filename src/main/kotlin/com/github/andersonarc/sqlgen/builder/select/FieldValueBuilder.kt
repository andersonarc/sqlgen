package com.github.andersonarc.sqlgen.builder.select

import com.github.andersonarc.sqlgen.serialization.reflection.FieldWrapper

class FieldValueBuilder(val field: FieldWrapper) : ValueBuilder(field.name)