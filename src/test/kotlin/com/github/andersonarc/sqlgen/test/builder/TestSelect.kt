package com.github.andersonarc.sqlgen.test.builder

import org.junit.Test


class SelectBuilder<T>(val clazz: Class<T>) {
    fun execute(): List<T> {
        TODO()
    }

    fun where(expression: (WhereExpressionBuilder<Class<T>>) -> WhereExpressionBuilder<Class<T>>) {
        val builder = WhereExpressionBuilder(clazz)
        expression(builder)
    }
}

class WhereExpressionBuilder<T: Class<*>>(val clazz: T) {
    private val tokens = ArrayList<String>()

    fun build(): String {
        return tokens.joinToString(" ") { it }
    }

    operator fun get(field: String): WhereExpressionBuilder<T> {
        tokens.add(field)
        return this
    }

    infix fun eq(other: WhereExpressionBuilder<T>): WhereExpressionBuilder<T> {
        tokens.add("==")
        return this
    }

    infix fun neq(other: WhereExpressionBuilder<T>): WhereExpressionBuilder<T> {
        tokens.add("!=")
        return this
    }
}

class SelectFieldBuilder<T : Class<*>>(val clazz: T) {
    operator fun get(s: String): SelectFieldBuilder<T> {
        TODO()
        return this
    }

    operator fun plus(b: SelectFieldBuilder<Class<Any>>): SelectFieldBuilder<Class<Any>> {
        TODO()
    }
}

fun <T> select(clazz: Class<T>): SelectBuilder<T> {
    return SelectBuilder(clazz)
}

fun <T> select(
    clazz: Class<T>,
    par1: (SelectFieldBuilder<Class<T>>) -> SelectFieldBuilder<Class<*>>): SelectBuilder<T> {

    var b = SelectFieldBuilder(clazz)
    par1(b)
    return SelectBuilder(clazz)
}


class TestSelect {
    class SampleInt {
        var value1 = 1
        var value2 = 2
    }

    @Test
    fun testSelect() {
        val res: List<SampleInt> =
            select(SampleInt::class.java)
            .execute()
    }

    @Test
    fun testWhereGreaterThan() {
        val res =
            select(SampleInt::class.java)
            .where(
                { it["value1"] > it["value2"] }
            )
            .execute()
    }

    @Test
    fun testWhereGreaterThan() {
        val res =
            select(SampleInt::class.java)
                .where(
                    { it["value1"] gt it["value2"] }
                )
                .execute()
    }

    @Test
    fun testWhereLessThan() {
        val res =
            select(SampleInt::class.java)
            .where(
                { it["value1"] < it["value2"] }
            )
            .execute()
    }

    @Test
    fun testWhereLessThan() {
        val res =
            select(SampleInt::class.java)
                .where(
                    { it["value1"] lt it["value2"] }
                )
                .execute()
    }

    @Test
    fun testWhereGreaterThanOrEqual() {
        val res =
            select(SampleInt::class.java)
            .where(
                { it["value1"] >= it["value2"] }
            )
            .execute()
    }

    @Test
    fun testWhereGreaterThanOrEqual() {
        val res =
            select(SampleInt::class.java)
                .where(
                    { it["value1"] gte it["value2"] }
                )
                .execute()
    }

    @Test
    fun testWhereLessThanOrEqual() {
        val res =
            select(SampleInt::class.java)
            .where(
                { it["value1"] <= it["value2"] }
            )
            .execute()
    }

    @Test
    fun testWhereLessThanOrEqual() {
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

//    @Test
//    fun test() {
//        select(SampleInt::class.java,
//             { it["value1"] + it["value2"] }
//            )
//            .execute()
//    }





}