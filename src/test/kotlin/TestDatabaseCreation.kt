import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths
import java.sql.DriverManager
import javax.xml.crypto.Data

val testDbPath = Paths.get("test.db").toAbsolutePath().toString()
val testDbUrl = "jdbc:sqlite:$testDbPath"

class TestDatabaseCreation {
    lateinit var db: Database

    init {
        DriverManager.registerDriver(org.sqlite.JDBC())
    }

    @Before
    fun deleteDb() {
        Files.deleteIfExists(Paths.get(testDbPath))
        db = Database(testDbUrl)
    }

    @Test
    fun testInt() {
        class TestInt {
            var x = 5
        }

        db.createTable(
            TestInt::class.java
        )

        db.executeSQL(
            "INSERT INTO TestInt VALUES (1)"
        )

        db.executeSQL(
            "INSERT INTO TestInt VALUES (2)"
        )

        val stmt = db.executeSQL(
            "SELECT count(*) FROM TestInt"
        )

        val set = stmt.resultSet

        set.first()
        val value = set.getInt(0)
        assertEquals(value, 2)

        set.last()
        assertEquals(set.row, 1)
    }

}