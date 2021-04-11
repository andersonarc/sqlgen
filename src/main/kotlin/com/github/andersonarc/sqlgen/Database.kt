package com.github.andersonarc.sqlgen

import com.github.andersonarc.sqlgen.serialization.getClassWrapper
import com.github.andersonarc.sqlgen.serialization.getSerializableFields
import com.github.andersonarc.sqlgen.serialization.javaClassToTableName
import com.github.andersonarc.sqlgen.serialization.reflection.FieldValueWrapper
import com.github.andersonarc.sqlgen.serialization.reflection.createClassInstance
import com.github.andersonarc.sqlgen.serialization.sqlValueToJavaValue
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

class Database(url: String) {
    private val connection = DriverManager.getConnection(url)

    fun executeSql(sql: String): Statement {
        println(sql)
        val statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
        statement.execute(sql)
        return statement
    }

    fun executeSqlMultiple(sql: List<String>): Statement {
        println(sql)
        val statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
        for (value in sql) {
            statement.execute(value)
        }
        return statement
    }

    //todo use SQL statements to prevent injections

    fun createTable(clazz: Class<*>) {
        val wrapper = getClassWrapper(clazz)
        val sql = wrapper.fields.joinToString(
            ", ",
            "CREATE TABLE ${wrapper.tableName} (", ")"
        ) {
            it.field.name + ' ' + it.sqlType
        }
        executeSql(sql)
    }

    fun insertValue(instance: Any) {
        val wrapper = getClassWrapper(instance.javaClass)
        val sql = wrapper.fields.joinToString(
            ", ",
            "INSERT INTO ${wrapper.tableName} VALUES (", ")"
        ) {
            it.getSqlValue(instance)
        }
        executeSql(sql)
    }

    fun insertValues(vararg instances: Any) {
        val wrapper = getClassWrapper(instances.javaClass.componentType)
        val sql = instances.map { instance ->
            wrapper.fields.joinToString(
                ", ",
                "INSERT INTO ${wrapper.tableName} VALUES (", ")"
            ) {
                it.getSqlValue(instance)
            }
        }
        executeSqlMultiple(sql)
    }

    //todo class cache with fields

    fun <T> selectAll(clazz: Class<T>): List<T> {
        val sql = "SELECT * FROM " + javaClassToTableName(clazz)
        val result = executeSql(sql).resultSet

        val fields = getSerializableFields(clazz)
        val args = mutableListOf<FieldValueWrapper>()
        val list = mutableListOf<T>()

        while (result.next()) {
            for (field in fields) {
                args.add(FieldValueWrapper(field.field, sqlValueToJavaValue(field.field, result)))
            }
            list.add(createClassInstance(clazz, args))
        }

        return list
    }

    fun closeConnection() {
        connection.close()
    }
}