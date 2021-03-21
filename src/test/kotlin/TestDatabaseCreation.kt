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

        val values = intArrayOf(1, 2)

        db.createTable(
            TestInt::class.java
        )

        for (value in values) {
            db.executeSQL(
                "INSERT INTO TestInt VALUES ($value)"
            )
        }

        val stmt = db.executeSQL(
            "SELECT * FROM TestInt"
        )

        val set = stmt.resultSet
        var count = 0
        while (set.next()) {
            assertEquals(values[count], set.getInt(1))
            count++
        }
        assertEquals(2, count)

        val stmt2 = db.executeSQL(
            "SELECT count(*) FROM TestInt"
        )

        val set2 = stmt2.resultSet
        assertEquals(2, set2.getInt(1))
    }

}