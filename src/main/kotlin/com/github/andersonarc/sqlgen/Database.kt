package com.github.andersonarc.sqlgen

import com.github.andersonarc.sqlgen.serialization.getClassWrapper
import com.github.andersonarc.sqlgen.serialization.reflection.FieldValueWrapper
import com.github.andersonarc.sqlgen.serialization.sqlValueToJavaValue
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

class Database(url: String) {
    private val connection = DriverManager.getConnection(url)

    fun executeQuery(sql: String): ResultSet {
        println(sql)
        return connection.createStatement().executeQuery(sql)
    }

    fun executeUpdate(sql: String) {
        println(sql)
        connection.createStatement().executeUpdate(sql)
    }

    fun executePreparedUpdate(statement: PreparedStatement) {
        println(statement)
        statement.executeUpdate()
    }

    fun executePreparedQuery(statement: PreparedStatement): ResultSet {
        println(statement)
        return statement.executeQuery()
    }

    fun prepareSQL(template: String): PreparedStatement {
        println(template)
        return connection.prepareStatement(template)
    }

    fun createTable(clazz: Class<*>) {
        val wrapper = getClassWrapper(clazz)
        val sql = wrapper.fields.joinToString(
            ", ",
            "CREATE TABLE ${wrapper.tableName} (", ")"
        ) { it.field.name + " " + it.sqlType.toString() }
        executeUpdate(sql)
    }

    fun insertValue(instance: Any) {
        val wrapper = getClassWrapper(instance.javaClass)
        val sql = wrapper.fields.joinToString(
            ", ",
            "INSERT INTO ${wrapper.tableName} VALUES (", ")"
        ) { "?" }
        val stmt = prepareSQL(sql)
        wrapper.fields.forEachIndexed { i, field ->
            field.insertIntoStatement(instance, 1 + i, stmt)
        }
        executePreparedUpdate(stmt)
    }

    fun insertValues(vararg instances: Any) {
        val wrapper = getClassWrapper(instances.javaClass.componentType)
        val sql = wrapper.fields.joinToString(
            ", ",
            "INSERT INTO ${wrapper.tableName} VALUES (", ")"
        ) { "?" }
        val stmt = prepareSQL(sql)
        for (instance in instances) {
            wrapper.fields.forEachIndexed { i, field ->
                field.insertIntoStatement(instance, 1 + i, stmt)
            }
            executePreparedUpdate(stmt)
        }
    }

    fun <T> selectAll(clazz: Class<T>): List<T> {
        val wrapper = getClassWrapper(clazz)
        val result = executeQuery("SELECT * FROM " + wrapper.tableName)

        val args = mutableListOf<FieldValueWrapper>()
        val list = mutableListOf<T>()

        while (result.next()) {
            for (field in wrapper.fields) {
                args.add(FieldValueWrapper(field.field, sqlValueToJavaValue(field.field, result)))
            }
            list.add(wrapper.createInstance(args))
        }

        return list
    }

    fun closeConnection() {
        connection.close()
    }
}