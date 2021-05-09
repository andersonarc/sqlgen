package com.github.andersonarc.sqlgen.test.builder

import com.github.andersonarc.sqlgen.builder.select.*
import com.github.andersonarc.sqlgen.test.DatabaseTest
import org.junit.Assert
import org.junit.Test


/*
class FieldBuilder<T> {}

fun <A, B, T> select(clazz: Class<T>, func1: (FieldBuilder<T>) -> FieldBuilder<A>) {

}

class WhereExpressionBuilder<T>() {
    private val tokens = ArrayList<String>()

    fun tokens(): List<String> {
        return this.tokens
    }

    operator fun get(field: String): WhereExpressionBuilder<T> {
        tokens.add(field)
        return this
    }

    override fun equals(other: Any?): Boolean {
        throw Exception("Use infix functions 'eq' and 'ne'")
    }

    operator fun compareTo(other: Any?): Int {
        throw Exception("Use infix functions 'eq', 'neq', 'ge', 'gte', 'le', 'lte'")
    }

    infix fun eq(other: WhereExpressionBuilder<T>): WhereExpressionBuilder<T> {
        tokens.add("==")
        return this
    }

    infix fun neq(other: WhereExpressionBuilder<T>): WhereExpressionBuilder<T> {
        tokens.add("!=")
        return this
    }

    infix fun gt(other: WhereExpressionBuilder<T>): WhereExpressionBuilder<T> {
        tokens.add(">")
        return this
    }

    infix fun gte(other: WhereExpressionBuilder<T>): WhereExpressionBuilder<T> {
        tokens.add(">=")
        return this
    }

    infix fun lt(other: WhereExpressionBuilder<T>): WhereExpressionBuilder<T> {
        tokens.add("<")
        return this
    }

    infix fun lte(other: WhereExpressionBuilder<T>): WhereExpressionBuilder<T> {
        tokens.add("<=")
        return this
    }
}

class SelectResultBuilder<T>(val parent: SelectConditionBuilder<T>) {
    private var tokens = emptyList<String>()

    fun tokens(): List<String> {
        return tokens
    }

    fun build(): String {
        return parent.build(this)
    }
}

class SelectConditionBuilder<T>(private val parent: SelectBuilder<T>) {
    private var tokens = emptyList<String>()

    fun tokens(): List<String> {
        return tokens
    }

    fun build(child: SelectResultBuilder<T>): String {
        return parent.build(this, child)
    }

    fun where(expression: (WhereExpressionBuilder<T>) -> WhereExpressionBuilder<T>): SelectResultBuilder<T> {
        tokens = expression(WhereExpressionBuilder()).tokens()
        return SelectResultBuilder(this)
    }
}

class SelectBuilder<T>(clazz: Class<T>) {
    private val wrapper = getClassWrapper(clazz)

    fun build(child: SelectConditionBuilder<T>, grandchild: SelectResultBuilder<T>): String {
        val tokens = mutableListOf<String>().apply {
            add("SELECT")
            addAll(child.tokens())
            add("FROM")
            add(wrapper.tableName)
            if (grandchild.)) {
                add("WHERE")
                addAll(whereTokens)
            }
        }
        return tokens.joinToString(" ")
    }

    fun execute(database: Database): List<T> {
        val result = database.executeQuery(build())
        val args = mutableListOf<FieldValueWrapper>()
        val list = mutableListOf<T>()
        while (result.next()) {
            for (field in wrapper.fields) {
                args.add(FieldValueWrapper(field.field, sqlValueToJavaValue(field.field, result)))
            }
            list.add(wrapper.createInstance(args))
        }
        return list
    }

    fun all(): SelectConditionBuilder<T> {
        targetTokens = listOf("*")
        return SelectConditionBuilder(this)
    }
}

fun <T> select(clazz: Class<T>): SelectBuilder<T> {
    return SelectBuilder(clazz)
}

*/

class TestSelect() : DatabaseTest() {
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
    fun testSelectFiled1() {
        ignoreThrows {
            var list: List<Int> =
                select<SampleInt>()
                    .field<Int> { it["value1"] + it["value2"] }
                    .execute(db)
        }
    }

    @Test
    fun testSelectFiled2() {
        ignoreThrows {
            var list: List<Tuple2<Int, Int>> =
                select<SampleInt>()
                    .field<Int> { it["value1"] + it["value2"] }
                    .field<Int> { it["value2"] * it["value3"] }
                    .execute(db)
        }
    }

    @Test
    fun testSelectFiled3() {
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
    fun testSelectFiled4() {
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