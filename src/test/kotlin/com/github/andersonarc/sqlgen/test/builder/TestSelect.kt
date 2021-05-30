package com.github.andersonarc.sqlgen.test.builder

import com.github.andersonarc.sqlgen.builder.select.*
import com.github.andersonarc.sqlgen.test.DatabaseTest
import org.junit.Assert
import org.junit.Test

class TestSelect : DatabaseTest() {
    class SampleInt {
        var value1 = 1
        var value2 = 2
        var value3 = 3
        var value4 = 4
        var value5 = 5
    }

    private inline fun <reified T : Throwable> assertThrows(block: () -> Unit) {
        try {
            block()
        } catch (e: Throwable) {
            if (!e::class.java.isAssignableFrom(T::class.java)) {
                Assert.fail("Block threw unexpected exception: ${e::class.java.simpleName}, expected ${T::class.java.simpleName}")
            } else {
                return
            }
        }
        Assert.fail("Block didn't throw an exception, expected ${T::class.java.simpleName}")
    }

    private fun ignoreThrows(block: () -> Unit) {
        try {
            block()
        } catch (t: Throwable) {

        }
    }

    @Test
    fun testSelectClass() {
        ignoreThrows {
            var list: List<SampleInt> =
                select<SampleInt>()
                    .execute(db)
        }
    }

    @Test
    fun testSelectField1() {
        ignoreThrows {
            var list: List<Int> =
                select<SampleInt>()
                    .field<Int> { it["value1"] + it["value2"] }
                    .execute(db)
        }
    }

    @Test
    fun testSelectField2() {
        ignoreThrows {
            var list: List<Tuple2<Int, Int>> =
                select<SampleInt>()
                    .field<Int>("column1") { it["value1"] + it["value2"] }
                    .field<Int>("column1") { it["value2"] * it["value3"] }
                    .execute(db)
        }
    }

    @Test
    fun testSelectField3() {
        ignoreThrows {
            var list: List<Tuple3<Int, Int, Int>> =
                select<SampleInt>()
                    .field<Int> { it["value1"] + it["value2"] }
                    .field<Int> { it["value2"] * it["value3"] }
                    .field<Int> { it["value4"] + it["value5"] + 5 }
                    .execute(db)
        }
    }

    @Test
    fun testSelectField4() {
        ignoreThrows {
            var list: List<Tuple4<Int, Int, Int, Int>> =
                select<SampleInt>()
                    .field<Int> { it["value1"] + it["value2"] }
                    .field<Int> { it["value2"] * it["value3"] }
                    .field<Int> { it["value4"] + it["value5"] + 5 }
                    .field<Int> { it["value4"] + it["value5"] + 5 }
                    .execute(db)
        }
    }

    @Test
    fun testSelectFiled5() {
        ignoreThrows {
            var list: List<Tuple5<Int, Int, Int, Int, Int>> =
                select<SampleInt>()
                    .field<Int> { it["value1"] + it["value2"] }
                    .field<Int> { it["value2"] * it["value3"] }
                    .field<Int> { it["value4"] + it["value5"] + 5 }
                    .field<Int> { it["value4"] + it["value5"] + 5 }
                    .field<Int> { it["value4"] + it["value5"] + 5 }
                    .execute(db)
        }
    }

    @Test
    fun testSelectField2WithColumnNames() {
        ignoreThrows {
            var list: List<Tuple2<Int, Int>> =
                select<SampleInt>()
                    .field<Int>("column1") { it["value1"] + it["value2"] }
                    .field<Int>("column2") { it["value2"] * it["value3"] }
                    .execute(db)
        }
    }

    @Test
    fun testSelectWrongField() {
        assertThrows<IllegalArgumentException> {
            select<SampleInt>()
                .field<Int> { it["unknown"] + it["value2"] }
                .build()
        }
    }

    /*

    @Test
    fun testWhereGreaterThanInfix() {
        val res =
            select(SampleInt::class.java)
                .where { it["value1"] gt it["value2"] }
                .execute(db)
    }

    @Test
    fun testWhereLessThanInfix() {
        val res =
            select(SampleInt::class.java)
                .where(
                    { it["value1"] lt it["value2"] }
                )
                .execute()
    }

    @Test
    fun testWhereGreaterThanOrEqualInfix() {
        val res =
            select(SampleInt::class.java)
                .where(
                    { it["value1"] gte it["value2"] }
                )
                .execute()
    }

    @Test
    fun testWhereLessThanOrEqualInfix() {
        val res =
            select(SampleInt::class.java)
                .where(
                    { it["value1"] lte it["value2"] }
                )
                .execute()
    }

    @Test
    fun testWhereEqualsInfix() {
        val res =
            select(SampleInt::class.java)
            .where(
                { it["value1"] eq it["value2"] }
            )
            .execute()
    }

    @Test
    fun testWhereNotEqualsInfix() {
        val res =
            select(SampleInt::class.java)
                .where(
                    { it["value1"] neq it["value2"] }
                )
                .execute()
    }

    */
}