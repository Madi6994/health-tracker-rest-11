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
                assertEquals(Calories.get(3), exercisegoalsDAO.findByexerciseId(Calories.get(3).id))
                assertEquals(Calories.get(4), exercisegoalsDAO.findByexerciseId(Calories.get(4).id))
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
                assertEquals(3, CaloriesDAO.getAll().size)
            }
        }


        @Test
        fun `get activity by user id that has no activities, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val CaloriesDAO = populateExercise_goalsTable()
                //Act & Assert
                assertEquals(0, CaloriesDAO.findByUserId(3).size)
            }
        }


        @Test
        fun `get activity by user id that exists, results in a correct activitie(s) returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val CaloriesDAO = populateExercise_goalsTable()
                //Act & Assert
                assertEquals(Calories.get(0), CaloriesDAO.findByUserId(1).get(0))
                assertEquals(Calories.get(1), CaloriesDAO.findByUserId(1).get(1))
                assertEquals(Calories.get(2), CaloriesDAO.findByUserId(2).get(0))
                assertEquals(Calories.get(3), CaloriesDAO.findByUserId(1).get(1))
                assertEquals(Calories.get(4), CaloriesDAO.findByUserId(1).get(0))
            }
        }


        @Test
        fun `get all activities over empty table returns none`() {
            transaction {

                //Arrange - create and setup activityDAO object
                SchemaUtils.create(Exercise_goal)
                val CaloriesDAO = ExercisegoalsDAO()

                //Act & Assert
                assertEquals(0, CaloriesDAO.getAll().size)
            }
        }


        @Test
        fun `get activity by activity id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val CaloriesDAO = populateExercise_goalsTable()
                //Act & Assert
                assertEquals(null, CaloriesDAO.findByexerciseId(4))
            }
        }

        @Test
        fun `get activity by activity id that exists, results in a correct activity returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val CaloriesDAO = populateExercise_goalsTable()
                //Act & Assert
                assertEquals(Calories.get(1), CaloriesDAO.findByexerciseId(1))
                assertEquals(Calories.get(3), CaloriesDAO.findByexerciseId(3))
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
                val CaloriesDAO = populateExercise_goalsTable()

                //Act & Assert
                val Caloriesupdate = Exercise_goals(id = 1, Calories_To_Burn = 155, Steps = 65, Date = DateTime.now(), userId = 1)
                CaloriesDAO.updateByexerciseId(Caloriesupdate.id, Caloriesupdate)
                assertEquals(Caloriesupdate, CaloriesDAO.findByexerciseId(3))
            }
        }

        @Test
        fun `updating non-existant activity in table results in no updates`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val CaloriesDAO = populateExercise_goalsTable()

                //Act & Assert
                val Caloriesupdate = Exercise_goals(id = 1, Calories_To_Burn = 155, Steps = 65, Date = DateTime.now(), userId = 1)
                CaloriesDAO.updateByexerciseId(4, Caloriesupdate)
                assertEquals(null, CaloriesDAO.findByexerciseId(4))
                assertEquals(5, CaloriesDAO.getAll().size)
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
                val CaloriesDAO = populateExercise_goalsTable()

                //Act & Assert
                assertEquals(5, CaloriesDAO.getAll().size)
                CaloriesDAO.deleteByexerciseId(4)
                assertEquals(5, CaloriesDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing activity (by id) in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val CaloriesDAO = populateExercise_goalsTable()

                //Act & Assert
                assertEquals(5, CaloriesDAO.getAll().size)
                CaloriesDAO.deleteByexerciseId(activity3.id)
                assertEquals(3, CaloriesDAO.getAll().size)
            }
        }

        @Test
        fun `deleting activities when none exist for user id results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val CaloriesDAO = populateExercise_goalsTable()

                //Act & Assert
                assertEquals(3, CaloriesDAO.getAll().size)
                CaloriesDAO.deleteByUserId(3)
                assertEquals(3, CaloriesDAO.getAll().size)
            }
        }



        @Test
        fun `deleting activities when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val CaloriesDAO = populateExercise_goalsTable()

                //Act & Assert
                assertEquals(3, CaloriesDAO.getAll().size)
                CaloriesDAO.deleteByUserId(1)
                assertEquals(1, CaloriesDAO.getAll().size)
            }
        }
    }


}