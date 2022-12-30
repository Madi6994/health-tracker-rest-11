package ie.setu.repository

import ie.setu.domain.Activity
import ie.setu.domain.Exercise_goals
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Exercise_goal
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.ExercisegoalsDAO
import ie.setu.helpers.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals



private val calories1 = Calories.get(0)
private val calories2 = Calories.get(1)
private val calories3 = Calories.get(2)
private val calories4 = Calories.get(3)
private val calories5 = Calories.get(4)



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
                assertEquals(5, exercisegoalsDAO.getAll().size)
                assertEquals(calories1, exercisegoalsDAO.findByexerciseId(Calories.get(0).id))
                assertEquals(calories2, exercisegoalsDAO.findByexerciseId(Calories.get(1).id))
                assertEquals(calories3, exercisegoalsDAO.findByexerciseId(Calories.get(2).id))
                assertEquals(calories4, exercisegoalsDAO.findByexerciseId(Calories.get(3).id))
                assertEquals(calories5, exercisegoalsDAO.findByexerciseId(Calories.get(4).id))
            }
        }
    }


    @Nested
    inner class ReadActivities {

        @Test
        fun `getting all activites from a populated table returns all rows`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val CaloriesDAO = populateExercise_goalsTable()
                //Act & Assert
                assertEquals(5, CaloriesDAO.getAll().size)
            }
        }


        @Test
        fun `get activity by user id that has no activities, results in no record returned`() {
             transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val caloriesDAO = populateExercise_goalsTable()
                //Act & Assert
                assertEquals(0, caloriesDAO.findByUserId(5).size)
            }
        }


        @Test
        fun `get activity by user id that exists, results in a correct activitie(s) returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val CaloriesDAO = populateExercise_goalsTable()
                //Act & Assert
                assertEquals(calories1, CaloriesDAO.findByUserId(1).get(0))
                assertEquals(calories2, CaloriesDAO.findByUserId(1).get(1))
                assertEquals(calories3, CaloriesDAO.findByUserId(2).get(0))
                assertEquals(calories4, CaloriesDAO.findByUserId(1).get(1))
                assertEquals(calories5, CaloriesDAO.findByUserId(1).get(0))
            }
        }


        @Test
        fun `get all activities over empty table returns none`() {
            transaction {

                //Arrange - create and setup activityDAO object
                SchemaUtils.create(Exercise_goal)
                val caloriesDAO = ExercisegoalsDAO()

                //Act & Assert
                assertEquals(0, caloriesDAO.getAll().size)
            }
        }


        @Test
        fun `get activity by activity id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val caloriesDAO = populateExercise_goalsTable()
                //Act & Assert
                assertEquals(null, caloriesDAO.findByexerciseId(6))
            }
        }

        @Test
        fun `get activity by activity id that exists, results in a correct activity returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val caloriesDAO = populateExercise_goalsTable()
                //Act & Assert
                assertEquals(calories1, caloriesDAO.findByexerciseId(1))
                assertEquals(calories3, caloriesDAO.findByexerciseId(3))
            }
        }
    }

    @Nested
    inner class UpdateActivities {
        @Test
        fun `updating existing activity in table results in successful update`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val caloriesDAO = populateExercise_goalsTable()

                //Act & Assert
                val calories3update = Exercise_goals(id = 3, Calories_To_Burn = 155, Steps = 65, Date = DateTime.now(), userId = 1)
                caloriesDAO.updateByexerciseId(calories3update.id, calories3update)
                assertEquals(calories3update, caloriesDAO.findByexerciseId(3))
            }
        }

        @Test
        fun `updating non-existant activity in table results in no updates`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val caloriesDAO = populateExercise_goalsTable()

                //Act & Assert
                val caloriesupdate = Exercise_goals(id = 6, Calories_To_Burn = 155, Steps = 65, Date = DateTime.now(), userId = 1)
                caloriesDAO.updateByexerciseId(6, caloriesupdate)
                assertEquals(null, caloriesDAO.findByexerciseId(4))
                assertEquals(5, caloriesDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteActivities {
        @Test
        fun `deleting a non-existant activity (by id) in table results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val caloriesDAO = populateExercise_goalsTable()

                //Act & Assert
                assertEquals(5, caloriesDAO.getAll().size)
                caloriesDAO.deleteByexerciseId(6)
                assertEquals(5, caloriesDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing activity (by id) in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val caloriesDAO = populateExercise_goalsTable()

                //Act & Assert
                assertEquals(5, caloriesDAO.getAll().size)
                caloriesDAO.deleteByexerciseId(calories3.id)
                assertEquals(3, caloriesDAO.getAll().size)
            }
        }

        @Test
        fun `deleting activities when none exist for user id results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val caloriesDAO = populateExercise_goalsTable()

                //Act & Assert
                assertEquals(5, caloriesDAO.getAll().size)
                caloriesDAO.deleteByUserId(5)
                assertEquals(5, caloriesDAO.getAll().size)
            }
        }



        @Test
        fun `deleting activities when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val caloriesDAO = populateExercise_goalsTable()

                //Act & Assert
                assertEquals(5, caloriesDAO.getAll().size)
                caloriesDAO.deleteByUserId(1)
                assertEquals(1, caloriesDAO.getAll().size)
            }
        }
    }


}