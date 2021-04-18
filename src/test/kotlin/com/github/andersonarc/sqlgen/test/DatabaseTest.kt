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
        val result = db.executeQuery(
            "SELECT * FROM $tableName"
        )
        while (result.next()) {
            block(result)
        }
    }

    protected fun forEachTableRowIndexed(tableName: String, block: (ResultSet, Int) -> Unit) {
        val result = db.executeQuery(
            "SELECT * FROM $tableName"
        )
        var index = 0
        while (result.next()) {
            block(result, index)
            index++
        }
    }
}