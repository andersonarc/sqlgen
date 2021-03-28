package com.github.andersonarc.sqlgen.test

import com.github.andersonarc.sqlgen.Database
import org.junit.After
import org.junit.Before
import java.nio.file.Files
import java.nio.file.Paths
import java.sql.DriverManager

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
}