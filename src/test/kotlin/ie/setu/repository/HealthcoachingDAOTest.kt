package ie.setu.repository

import ie.setu.domain.Exercise_goals
import ie.setu.domain.Health_Coaching
import ie.setu.domain.db.Exercise_goal
import ie.setu.domain.db.Health_coaching
import ie.setu.domain.repository.ExercisegoalsDAO
import ie.setu.domain.repository.HealthcoachingDAO
import ie.setu.helpers.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals




private val calories3 = Calories.get(2)



class HealthcoachingDAOTest {

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
                val HealthcoachDAO = populateHealth_CoachingTable()
                //Act & Assert
                assertEquals(5, HealthcoachDAO.getAll().size)
                assertEquals(Coaching.get(0), HealthcoachDAO.findBycoachingId(Coaching.get(0).ID))
                assertEquals(Coaching.get(1), HealthcoachDAO.findBycoachingId(Coaching.get(1).ID))
                assertEquals(Coaching.get(2), HealthcoachDAO.findBycoachingId(Coaching.get(2).ID))
                assertEquals(Coaching.get(3), HealthcoachDAO.findBycoachingId(Coaching.get(3).ID))
                assertEquals(Coaching.get(4), HealthcoachDAO.findBycoachingId(Coaching.get(4).ID))
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
                val HealthcoachDAO = populateHealth_CoachingTable()
                //Act & Assert
                assertEquals(5, HealthcoachDAO.getAll().size)
            }
        }


        @Test
        fun `get activity by user id that has no activities, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val HealthcoachDAO = populateHealth_CoachingTable()
                //Act & Assert
                assertEquals(1, HealthcoachDAO.findByUserId(3).size)
            }
        }


        @Test
        fun `get activity by user id that exists, results in a correct activitie(s) returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val HealthcoachDAO = populateHealth_CoachingTable()
                //Act & Assert
                assertEquals(Coaching.get(0), HealthcoachDAO.findByUserId(1).get(0))
                assertEquals(Coaching.get(1), HealthcoachDAO.findByUserId(2).get(0))
                assertEquals(Coaching.get(2), HealthcoachDAO.findByUserId(3).get(0))
                assertEquals(Coaching.get(3), HealthcoachDAO.findByUserId(4).get(0))
                assertEquals(Coaching.get(4), HealthcoachDAO.findByUserId(5).get(0))
            }
        }


        @Test
        fun `get all activities over empty table returns none`() {
            transaction {

                //Arrange - create and setup activityDAO object
                SchemaUtils.create(Health_coaching)
                val HealthcoachDAO = HealthcoachingDAO()

                //Act & Assert
                assertEquals(0, HealthcoachDAO.getAll().size)
            }
        }


        @Test
        fun `get activity by activity id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val HealthcoachDAO = populateHealth_CoachingTable()
                //Act & Assert
                assertEquals(null, HealthcoachDAO.findBycoachingId(9))
            }
        }

        @Test
        fun `get activity by activity id that exists, results in a correct activity returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val HealthcoachDAO = populateHealth_CoachingTable()
                //Act & Assert
                assertEquals(Coaching.get(0), HealthcoachDAO.findBycoachingId(1))
                assertEquals(Coaching.get(0), HealthcoachDAO.findBycoachingId(1))
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
                val HealthcoachDAO = populateHealth_CoachingTable()

                //Act & Assert
                val Coachingupdate = Health_Coaching(ID = 1, Protein_Intake = 98, macro_percentages = 55, UserID = 1)
                HealthcoachDAO.updateBycoachId(Coachingupdate.ID, Coachingupdate)
                assertEquals(Coachingupdate, HealthcoachDAO.findBycoachingId(1))
            }
        }

        @Test
        fun `updating non-existant activity in table results in no updates`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val HealthcoachDAO = populateHealth_CoachingTable()

                //Act & Assert
                val Coachingupdate = Health_Coaching(ID = 1, Protein_Intake = 98, macro_percentages = 55, UserID = 1)
                HealthcoachDAO.updateBycoachId(4, Coachingupdate)
                assertEquals(null, HealthcoachDAO.findBycoachingId(9))
                assertEquals(5, HealthcoachDAO.getAll().size)
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
                val HealthcoachDAO = populateHealth_CoachingTable()

                //Act & Assert
                assertEquals(5, HealthcoachDAO.getAll().size)
                HealthcoachDAO.deleteBycoachId(4)
                assertEquals(4, HealthcoachDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing activity (by id) in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val HealthcoachDAO = populateHealth_CoachingTable()

                //Act & Assert
                assertEquals(5, HealthcoachDAO.getAll().size)
                HealthcoachDAO.deleteBycoachId(calories3.id)
                assertEquals(4, HealthcoachDAO.getAll().size)
            }
        }

        @Test
        fun `deleting activities when none exist for user id results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val HealthcoachDAO = populateHealth_CoachingTable()

                //Act & Assert
//                assertEquals(3, HealthcoachDAO.getAll().size)
                HealthcoachDAO.deleteByUserId(3)
                assertEquals(4, HealthcoachDAO.getAll().size)
            }
        }



        @Test
        fun `deleting activities when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val CaloriesDAO = populateExercise_goalsTable()

                //Act & Assert
//                assertEquals(3, CaloriesDAO.getAll().size)
                CaloriesDAO.deleteByUserId(1)
                assertEquals(4, CaloriesDAO.getAll().size)
            }
        }
    }





}