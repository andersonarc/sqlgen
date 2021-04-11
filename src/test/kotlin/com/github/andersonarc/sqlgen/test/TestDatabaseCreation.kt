package com.github.andersonarc.sqlgen.test

import com.github.andersonarc.sqlgen.serialization.javaClassToTableName
import org.junit.Assert.assertEquals
import org.junit.Test

class TestDatabaseCreation: DatabaseTest() {
    @Test
    fun testCreateSingleIntTable() {
        class TestInt {
            var x = 5
        }

        val values = intArrayOf(1, 2)

        val tableName = javaClassToTableName(TestInt::class.java)

        db.createTable(
            TestInt::class.java
        )

        for (value in values) {
            db.executeSql(
                "INSERT INTO $tableName VALUES ($value)"
            )
        }

        forEachTableRowIndexed(tableName) { set, index ->
            assertEquals(true, index < values.size)
            assertEquals(values[index], set.getInt(1))
        }

        val stmt = db.executeSql(
            "SELECT count(*) FROM $tableName"
        )
        val set = stmt.resultSet
        assertEquals(values.size, set.getInt(1))
    }

}