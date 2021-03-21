import java.nio.file.Paths
import java.sql.DriverManager

fun main() {
    DriverManager.registerDriver(org.sqlite.JDBC())
    val database = Database("jdbc:sqlite:" + Paths.get("test.db").toAbsolutePath())
}