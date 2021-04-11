package com.github.andersonarc.sqlgen.test

import com.github.andersonarc.sqlgen.Database
import org.junit.After
import org.junit.Before
import java.nio.file.Files
import java.nio.file.Paths
import java.sql.DriverManager
import java.sql.ResultSet

abstract class DatabaseTest {
    private val databasePath = Paths.get("test.db").toAbsolutePath()
    private val databaseURL = "jdbc:sqlite:$databasePath"
    protected lateinit var db: Database

    init {
        DriverManager.registerDriver(org.sqlite.JDBC())
    }

    @Before
    fun createNewDatabase() {
        db = Database(databaseURL)
    }

    @After
    fun deleteOldDatabase() {
        db.closeConnection()
        Files.deleteIfExists(databasePath)
    }

    protected fun forEachTableRow(tableName: String, block: (ResultSet) -> Unit) {
        val stmt = db.executeSql(
            "SELECT * FROM $tableName"
        )
        val set = stmt.resultSet
        while (set.next()) {
            block(set)
        }
    }

    protected fun forEachTableRowIndexed(tableName: String, block: (ResultSet, Int) -> Unit) {
        val stmt = db.executeSql(
            "SELECT * FROM $tableName"
        )
        val set = stmt.resultSet
        var index = 0
        while (set.next()) {
            block(set, index)
            index++
        }
    }
}