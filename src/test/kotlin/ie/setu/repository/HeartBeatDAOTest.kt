package ie.setu.repository

import ie.setu.domain.db.Activities
import ie.setu.domain.db.HeartRate
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.HeartBeatDAO
import ie.setu.helpers.populateUserTable
import ie.setu.helpers.rates
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

val obj1 = rates.get(0)
val obj2 = rates.get(1)
val obj3 = rates.get(2)

class HeartBeatDAOTest {

    companion object {

        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateheartRates {

        @Test
        fun  `multiple rates added to table can be retrieved successfully`(){
            transaction {

                val userDAO = populateUserTable()

                SchemaUtils.create(HeartRate)
                val HeartBeatDAO = HeartBeatDAO()

                HeartBeatDAO.save(obj1)
                HeartBeatDAO.save(obj2)
                HeartBeatDAO.save(obj3)

                assertEquals(3, HeartBeatDAO.getAll().size)
            }
        }
    }
}