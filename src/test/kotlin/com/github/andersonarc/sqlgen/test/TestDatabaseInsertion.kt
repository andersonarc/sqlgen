package com.github.andersonarc.sqlgen.test

import com.github.andersonarc.sqlgen.serialization.javaClassToTableName
import org.junit.Assert
import org.junit.Test

class TestDatabaseInsertion : DatabaseTest() {
    @Test
    fun testInsertIntoSingleIntTable() {
        class TestInt(var x: Int)

        val values = arrayOf(0, 2, 5)

        val tableName = javaClassToTableName(TestInt::class.java)

        db.createTable(TestInt::class.java)

        for (value in values) {
            db.insertValue(TestInt(value))
        }

        forEachTableRowIndexed(tableName) { set, index ->
            Assert.assertEquals(true, index < values.size)
            Assert.assertEquals(values[index], set.getInt(1))
        }

        val result = db.executeQuery(
            "SELECT count(*) FROM $tableName"
        )
        Assert.assertEquals(values.size, result.getInt(1))
    }

    @Test
    fun testInsertMultipleValuesIntoSingleIntTable() {
        class TestInt(var x: Int)

        val values = arrayOf(0, 2, 5)

        val tableName = javaClassToTableName(TestInt::class.java)

        db.createTable(TestInt::class.java)

        db.insertValues(*values.map { TestInt(it) }.toTypedArray())

        forEachTableRowIndexed(tableName) { set, index ->
            Assert.assertEquals(true, index < values.size)
            Assert.assertEquals(values[index], set.getInt(1))
        }

        val result = db.executeQuery(
            "SELECT count(*) FROM $tableName"
        )
        Assert.assertEquals(values.size, result.getInt(1))
    }

    @Test
    fun testDatabaseFieldOrderIsSortedAlphabetically() {
        class TestFields(var b: Int, var a: Int, var c: Int)

        val tableName = javaClassToTableName(TestFields::class.java)

        db.createTable(TestFields::class.java)

        val fieldNames = TestFields::class.java.declaredFields.sortedBy { it.name }

        forEachTableRowIndexed(tableName) { set, index ->
            Assert.assertEquals(true, index < fieldNames.size)
            Assert.assertEquals(fieldNames[index], set.metaData.getColumnName(index))
        }
    }
}