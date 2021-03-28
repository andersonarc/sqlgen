package com.github.andersonarc.sqlgen

import com.github.andersonarc.sqlgen.serialization.SQLSerializer
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

class Database(url: String) {
    private val connection = DriverManager.getConnection(url)


    fun executeSQL(string: String): Statement {
        println(string)
        val statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
        statement.execute(string)
        return statement
    }

    //todo use SQL statements to prevent injections

    fun createTable(clazz: Class<*>) {
        val sql = SQLSerializer.getSerializableFields(clazz).joinToString(
            ", \n",
            "CREATE TABLE ${SQLSerializer.javaClassToTableName(clazz)} (", ")"
        ) {
            it.field.name + ' ' + SQLSerializer.javaClassToSQLType(it.field.type)
        }
        executeSQL(sql)
    }

    fun insertValue(instance: Any) {
        val clazz = instance.javaClass
        val sql = SQLSerializer.getSerializableFields(clazz).joinToString(
            ", \n",
            "INSERT INTO ${SQLSerializer.javaClassToTableName(clazz)} VALUES (", ")"
        ) {
            SQLSerializer.javaValueToSQLValue(it.getValue(instance))
        }
        executeSQL(sql)
    }

    fun insertValues(vararg values: Any) {
        for (value in values) {
            insertValue(value)
        }
    }

    fun closeConnection() {
        connection.close()
    }
}