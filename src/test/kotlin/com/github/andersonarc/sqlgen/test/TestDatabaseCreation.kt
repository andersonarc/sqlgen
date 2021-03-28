package com.github.andersonarc.sqlgen.test

import org.junit.Assert.assertEquals
import org.junit.Test

class TestDatabaseCreation: DatabaseTest() {
    @Test
    fun testInt() {
        class TestInt {
            var x = 5
        }

        val values = intArrayOf(1, 2)

        db.createTable(
            TestInt::class.java
        )

        for (value in values) {
            db.executeSQL(
                "INSERT INTO TestInt VALUES ($value)"
            )
        }

        val stmt = db.executeSQL(
            "SELECT * FROM TestInt"
        )

        val set = stmt.resultSet
        var count = 0
        while (set.next()) {
            assertEquals(values[count], set.getInt(1))
            count++
        }
        assertEquals(2, count)

        val stmt2 = db.executeSQL(
            "SELECT count(*) FROM TestInt"
        )

        val set2 = stmt2.resultSet
        assertEquals(2, set2.getInt(1))
    }

}