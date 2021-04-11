package com.github.andersonarc.sqlgen

import com.github.andersonarc.sqlgen.serialization.*
import com.github.andersonarc.sqlgen.serialization.reflection.FieldValueWrapper
import com.github.andersonarc.sqlgen.serialization.reflection.createClassInstance
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
        val sql = getSerializableFields(clazz).joinToString(
            ", \n",
            "CREATE TABLE ${javaClassToTableName(clazz)} (", ")"
        ) {
            it.field.name + ' ' + javaClassToSQLType(it.field.type)
        }
        executeSQL(sql)
    }

    fun insertValue(instance: Any) {
        val clazz = instance.javaClass
        val sql = getSerializableFields(clazz).joinToString(
            ", \n",
            "INSERT INTO ${javaClassToTableName(clazz)} VALUES (", ")"
        ) {
            javaValueToSQLValue(it.getValue(instance))
        }
        executeSQL(sql)
    }

    fun insertValues(vararg values: Any) {
        for (value in values) {
            insertValue(value)
        }
    }

    //todo class cache with fields

    fun <T> selectAll(clazz: Class<T>): T {
        val sql = "SELECT * FROM " + javaClassToTableName(clazz)
        val result = executeSQL(sql).resultSet

        val fields = getSerializableFields(clazz)
        val args = mutableListOf<FieldValueWrapper>()
        while (result.next()) {
            for (field in fields) {
                args.add(FieldValueWrapper(field.field, sqlValueToJavaValue(field.field, result)))
            }
        }

        return createClassInstance(clazz, args)
    }

    fun closeConnection() {
        connection.close()
    }
}