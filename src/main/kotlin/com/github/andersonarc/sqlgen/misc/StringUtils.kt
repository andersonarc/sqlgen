package com.github.andersonarc.sqlgen.misc

fun String.toTitleCase(): String {
    return substring(0, 1).toUpperCase() + substring(1)
}