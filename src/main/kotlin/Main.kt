import java.nio.file.Paths
import java.sql.DriverManager

fun main() {
    val database = Paths.get("test.db").toAbsolutePath()
    val url = "jdbc:sqlite:$database"
    DriverManager.registerDriver(org.sqlite.JDBC())
    val connection = DriverManager.getConnection(url)
    connection.createStatement().use {
        it.execute("DROP TABLE TEST")
    }
}