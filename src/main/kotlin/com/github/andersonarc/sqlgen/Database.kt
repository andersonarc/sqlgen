package com.github.andersonarc.sqlgen

import com.github.andersonarc.sqlgen.serialization.exception.NotSerializableClassException
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

class Database(url: String) {
    private val connection = DriverManager.getConnection(url)

    fun executeSQL(string: String): Statement {
        val statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
        statement.execute(string)
        return statement
    }

    fun createTable(clazz: Class<*>) {
        val sql = clazz.fields.joinToString(", \n",
            "CREATE TABLE ${SQLSerializer.javaClassToTableName(clazz)} (", ")") {
            it.name + ' ' + SQLSerializer.javaClassToSQLType(it.type)
        }
        println(sql)
        executeSQL(sql)
    }

    fun insertValue(value: Any) {
        val clazz = value.javaClass
        val sql = clazz.fields.joinToString(", \n",
            "INSERT INTO ${SQLSerializer.javaClassToTableName(clazz)} VALUES (", ")") {
            SQLSerializer.javaValueToSQLValue(it.get(value))
        }
        println(sql)
        executeSQL(sql)
    }

    fun closeConnection() {
        connection.close()
    }
}