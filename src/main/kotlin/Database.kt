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

    private fun classToSQLType(clazz: Class<*>): String {
        return when (clazz) {
            Byte::class.java -> "TINYINT"
            Char::class.java -> "TINYINT"
            Short::class.java -> "SMALLINT"
            Int::class.java -> "INTEGER"
            Long::class.java -> "BIGINT"
            Float::class.java -> "FLOAT"
            Double::class.java -> "DOUBLE"
            Boolean::class.java -> "BOOL"
            String::class.java -> "VARCHAR($DEFAULT_VARCHAR_LENGTH)"
            CharArray::class.java -> "VARCHAR($DEFAULT_VARBINARY_SIZE)"
            ByteArray::class.java -> "VARBINARY($DEFAULT_VARBINARY_SIZE)"
            else -> throw NotSerializableClassException(clazz)
        }
    }

    fun createTable(clazz: Class<*>) {
        val sql = clazz.declaredFields.joinToString(", \n",
            "CREATE TABLE ${clazz.simpleName} (", ")") {
            it.name + ' ' + classToSQLType(it.type)
        }
        println(sql)
        executeSQL(sql)
    }

    companion object {
        private const val DEFAULT_VARCHAR_LENGTH = 255
        private const val DEFAULT_VARBINARY_SIZE = 255
    }
}