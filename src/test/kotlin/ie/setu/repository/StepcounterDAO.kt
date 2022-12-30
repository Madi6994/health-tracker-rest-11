package ie.setu.repository

import ie.setu.domain.Exercise_goals
import ie.setu.domain.Step_Counter
import ie.setu.domain.db.Exercise_goal
import ie.setu.domain.db.Step_counter
import ie.setu.domain.repository.ExercisegoalsDAO
import ie.setu.domain.repository.StepcounterDAO
import ie.setu.helpers.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.w3c.dom.css.Counter
import kotlin.test.assertEquals


private val calories3 = Calories.get(2)
class StepcounterDAOTest{

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
                val stepcounterDAO = populateStepCounterTable()
                //Act & Assert
                assertEquals(5, stepcounterDAO.getAll().size)
                assertEquals(Counter.get(0), stepcounterDAO.findByStepId(Counter.get(0).ID))
                assertEquals(Counter.get(1), stepcounterDAO.findByStepId(Counter.get(1).ID))
                assertEquals(Counter.get(2), stepcounterDAO.findByStepId(Counter.get(2).ID))
                assertEquals(Counter.get(3), stepcounterDAO.findByStepId(Counter.get(3).ID))
                assertEquals(Counter.get(4), stepcounterDAO.findByStepId(Counter.get(4).ID))
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
                val stepcounterDAO = populateStepCounterTable()
                //Act & Assert
                assertEquals(5, stepcounterDAO.getAll().size)
            }
        }


        @Test
        fun `get activity by user id that has no activities, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val stepcounterDAO = populateStepCounterTable()
                //Act & Assert
                assertEquals(1, stepcounterDAO.findByUserId(3).size)
            }
        }


        @Test
        fun `get activity by user id that exists, results in a correct activitie(s) returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val stepcounterDAO = populateStepCounterTable()
                //Act & Assert
                assertEquals(Counter.get(0), stepcounterDAO.findByUserId(1).get(0))
                assertEquals(Counter.get(1), stepcounterDAO.findByUserId(2).get(0))
                assertEquals(Counter.get(2), stepcounterDAO.findByUserId(3).get(0))
                assertEquals(Counter.get(3), stepcounterDAO.findByUserId(4).get(0))
                assertEquals(Counter.get(4), stepcounterDAO.findByUserId(5).get(0))
            }
        }


        @Test
        fun `get all activities over empty table returns none`() {
            transaction {

                //Arrange - create and setup activityDAO object
                SchemaUtils.create(Step_counter)
                val stepcounterDAO = StepcounterDAO()

                //Act & Assert
                assertEquals(0, stepcounterDAO.getAll().size)
            }
        }


        @Test
        fun `get activity by activity id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val stepcounterDAO = populateStepCounterTable()
                //Act & Assert
                assertEquals(null, stepcounterDAO.findByStepId(9))
            }
        }

        @Test
        fun `get activity by activity id that exists, results in a correct activity returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val stepcounterDAO = populateStepCounterTable()
                //Act & Assert
                assertEquals(Counter.get(0), stepcounterDAO.findByStepId(1))
                assertEquals(Counter.get(0), stepcounterDAO.findByStepId(1))
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
                val stepcounterDAO = populateStepCounterTable()

                //Act & Assert
                val Counterupdate = Step_Counter(ID = 1, Daily_Steps = 98, UserID = 1)
                stepcounterDAO.updateByStepId(Counterupdate.ID, Counterupdate)
                assertEquals(Counterupdate, stepcounterDAO.findByStepId(1))
            }
        }

        @Test
        fun `updating non-existant activity in table results in no updates`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val stepcounterDAO = populateStepCounterTable()

                //Act & Assert
                val Counterupdate = Step_Counter(ID = 1, Daily_Steps = 98, UserID = 1)
                stepcounterDAO.updateByStepId(4, Counterupdate)
                assertEquals(null, stepcounterDAO.findByStepId(9))
                assertEquals(5, stepcounterDAO.getAll().size)
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
                val stepcounterDAO = populateStepCounterTable()

                //Act & Assert
                assertEquals(5, stepcounterDAO.getAll().size)
                stepcounterDAO.deleteByStepId(4)
                assertEquals(4, stepcounterDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing activity (by id) in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val stepcounterDAO = populateStepCounterTable()

                //Act & Assert
                assertEquals(5, stepcounterDAO.getAll().size)
                stepcounterDAO.deleteByStepId(calories3.id)
                assertEquals(4, stepcounterDAO.getAll().size)
            }
        }

        @Test
        fun `deleting activities when none exist for user id results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val stepcounterDAO = populateStepCounterTable()

                //Act & Assert
//                assertEquals(3, stepcounterDAO.getAll().size)
                stepcounterDAO.deleteByUserId(3)
                assertEquals(4, stepcounterDAO.getAll().size)
            }
        }



        @Test
        fun `deleting activities when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val stepcounterDAO = populateStepCounterTable()

                //Act & Assert
//                assertEquals(1, stepcounterDAO.getAll().size)
                stepcounterDAO.deleteByUserId(1)
                assertEquals(4, stepcounterDAO.getAll().size)
            }
        }
    }






}