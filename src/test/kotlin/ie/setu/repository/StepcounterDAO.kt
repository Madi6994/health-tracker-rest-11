package ie.setu.repository

import ie.setu.helpers.activities
import org.jetbrains.exposed.sql.Database
import org.junit.jupiter.api.BeforeAll


private val activity3 = activities.get(2)
class StepcounterDAO {

    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }
}