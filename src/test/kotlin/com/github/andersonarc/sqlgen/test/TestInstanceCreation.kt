package com.github.andersonarc.sqlgen.test

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.lang.IllegalArgumentException

class TestInstanceCreation : DatabaseTest() {

    fun <T, V> genericTest(
        clazz: Class<T>,
        values: List<V>,
        init: (V) -> T,
        check: T.(V) -> Boolean,
    ) {
        db.createTable(clazz)

        values.forEach {
            db.insertValue(init(it) ?: throw IllegalArgumentException())
        }

        val result = db.selectAll(clazz)

        assertEquals(result.size, values.size)

        result.forEach {
            assertTrue(clazz.isInstance(it))
        }

        values.zip(result).forEach {
            assertTrue(it.second.check(it.first))
        }
    }

    @Test
    fun testSimpleInt() {
        class SimpleInt {
            var value: Int = 0
        }

        val values = listOf(2, 5, 7, 9)

        genericTest(
            SimpleInt::class.java,
            values,
            { SimpleInt().apply { value = it } },
            { it == value }
        )
    }

    @Test
    fun testSimpleDouble() {
        class SimpleDouble {
            var value: Double = 0.0
        }

        val values = listOf(2.1, 5.5, 7.9, 9.2)

        genericTest(
            SimpleDouble::class.java,
            values,
            { SimpleDouble().apply { value = it } },
            { it == value }
        )
    }

    @Test
    fun testSimpleString() {
        class SimpleString {
            var value: String = ""
        }

        val values = listOf("qwe", "asd", "dfg", "yui")

        genericTest(
            SimpleString::class.java,
            values,
            { SimpleString().apply { value = it } },
            { it == value }
        )
    }

    @Test
    fun testStringWithQuotes() {
        class SimpleString {
            var value: String = ""
        }

        val values = listOf("q\"we", "a\"s\"d", "dfg", "yui")

        genericTest(
            SimpleString::class.java,
            values,
            { SimpleString().apply { value = it } },
            { it == value }
        )
    }

    @Test
    fun testStringWithNulls() {
        class SimpleString {
            var value: String? = null
        }

        val values = listOf(null, "asd", "dfg", null, "ert")

        genericTest(
            SimpleString::class.java,
            values,
            { SimpleString().apply { value = it } },
            { it == value }
        )
    }
}