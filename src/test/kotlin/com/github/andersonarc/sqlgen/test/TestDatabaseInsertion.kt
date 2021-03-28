package com.github.andersonarc.sqlgen.test

import com.github.andersonarc.sqlgen.serialization.SQLSerializer
import org.junit.Assert
import org.junit.Test

class TestDatabaseInsertion : DatabaseTest() {
    @Test
    fun testInsertIntoSingleIntTable() {
        class TestInt(val x: Int)

        val values = arrayOf(0, 2, 5)

        val tableName = SQLSerializer.javaClassToTableName(TestInt::class.java)

        db.createTable(TestInt::class.java)

        for (value in values) {
            db.insertValue(TestInt(value))
        }

        forEachTableRowIndexed(tableName) { set, index ->
            Assert.assertEquals(true, index < values.size)
            Assert.assertEquals(values[index], set.getInt(1))
        }

        val stmt = db.executeSQL(
            "SELECT count(*) FROM $tableName"
        )
        val set = stmt.resultSet
        Assert.assertEquals(values.size, set.getInt(1))
    }
}