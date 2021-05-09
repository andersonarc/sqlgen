package com.github.andersonarc.sqlgen.builder.select

open class ValueBuilder(private vararg val tokens: String) { //todo ValueBuilder should have a value type
    operator fun plus(other: ValueBuilder): ValueBuilder {
        return ValueBuilder(*tokens(), "+", *other.tokens())
    }

    operator fun plus(other: Int): ValueBuilder {
        return ValueBuilder(*tokens(), "+", other.toString())
    }

    operator fun minus(other: ValueBuilder): ValueBuilder {
        return ValueBuilder(*tokens(), "-", *other.tokens())
    }

    operator fun minus(other: Int): ValueBuilder {
        return ValueBuilder(*tokens(), "-", other.toString())
    }

    operator fun times(other: ValueBuilder): ValueBuilder {
        return ValueBuilder(*tokens(), "*", *other.tokens())
    }

    operator fun times(other: Int): ValueBuilder {
        return ValueBuilder(*tokens(), "*", other.toString())
    }

    operator fun div(other: ValueBuilder): ValueBuilder {
        return ValueBuilder(*tokens(), "/", *other.tokens())
    }

    operator fun div(other: Int): ValueBuilder {
        return ValueBuilder(*tokens(), "/", other.toString())
    }

    fun tokens(): Array<out String> {
        return if (tokens.size == 1) tokens else arrayOf("(", *tokens, ")")
    }

    override fun toString(): String {
        return tokens.joinToString(" ")
    }
}