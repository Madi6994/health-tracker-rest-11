package ie.setu.repository

import ie.setu.helpers.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals



private val activity1 = activities.get(0)
private val activity2 = activities.get(1)
private val activity3 = activities.get(2)
private val activity4 = activities.get(3)
private val activity5 = activities.get(4)



class ExercisegoalsDAOTest {


    companion object {
        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }



    @Nested
    inner class CreateActivities {

        @Test
        fun `multiple activities added to table can be retrieved successfully`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val exercisegoalsDAO = populateExercise_goalsTable()
                //Act & Assert
                assertEquals(3, exercisegoalsDAO.getAll().size)
                assertEquals(Calories.get(0), exercisegoalsDAO.findByexerciseId(Calories.get(0).id))
                assertEquals(Calories.get(1), exercisegoalsDAO.findByexerciseId(Calories.get(1).id))
                assertEquals(Calories.get(2), exercisegoalsDAO.findByexerciseId(Calories.get(2).id))
            }
        }
    }
}