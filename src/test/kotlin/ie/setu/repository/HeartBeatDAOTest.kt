package ie.setu.repository

import ie.setu.domain.Exercise_goals
import ie.setu.domain.HeartBeat
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Exercise_goal
import ie.setu.domain.db.HeartRate
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.ExercisegoalsDAO
import ie.setu.domain.repository.HeartBeatDAO
import ie.setu.helpers.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

val obj1 = rates.get(0)
val obj2 = rates.get(1)
val obj3 = rates.get(2)


private val activity3 = activities.get(2)
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
    inner class CreateActivities {

        @Test
        fun `multiple activities added to table can be retrieved successfully`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val heartbeatDAO = populateHeartBeatTable()
                //Act & Assert
                assertEquals(3, heartbeatDAO.getAll().size)
                assertEquals(rates.get(0), heartbeatDAO.findByheartId(rates.get(0).id))
                assertEquals(rates.get(1), heartbeatDAO.findByheartId(rates.get(1).id))
                assertEquals(rates.get(2), heartbeatDAO.findByheartId(rates.get(2).id))
                assertEquals(rates.get(3), heartbeatDAO.findByheartId(rates.get(3).id))
                assertEquals(rates.get(4), heartbeatDAO.findByheartId(rates.get(4).id))
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
                val heartbeatDAO = populateHeartBeatTable()
                //Act & Assert
                assertEquals(3, heartbeatDAO.getAll().size)
            }
        }


        @Test
        fun `get activity by user id that has no activities, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val heartbeatDAO = populateHeartBeatTable()
                //Act & Assert
                assertEquals(0, heartbeatDAO.findByUserId(3).size)
            }
        }


        @Test
        fun `get activity by user id that exists, results in a correct activitie(s) returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val heartbeatDAO = populateHeartBeatTable()
                //Act & Assert
                assertEquals(rates.get(0), heartbeatDAO.findByUserId(1).get(0))
                assertEquals(rates.get(1), heartbeatDAO.findByUserId(1).get(1))
                assertEquals(rates.get(2), heartbeatDAO.findByUserId(2).get(0))
                assertEquals(rates.get(3), heartbeatDAO.findByUserId(1).get(1))
                assertEquals(rates.get(4), heartbeatDAO.findByUserId(1).get(0))
            }
        }


        @Test
        fun `get all activities over empty table returns none`() {
            transaction {

                //Arrange - create and setup activityDAO object
                SchemaUtils.create(HeartRate)
                val heartbeatDAO = HeartBeatDAO()

                //Act & Assert
                assertEquals(0, heartbeatDAO.getAll().size)
            }
        }


        @Test
        fun `get activity by activity id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val heartbeatDAO = populateHeartBeatTable()
                //Act & Assert
                assertEquals(null, heartbeatDAO.findByheartId(4))
            }
        }

        @Test
        fun `get activity by activity id that exists, results in a correct activity returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val heartbeatDAO = populateHeartBeatTable()
                //Act & Assert
                assertEquals(rates.get(1), heartbeatDAO.findByheartId(1))
                assertEquals(rates.get(3), heartbeatDAO.findByheartId(3))
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
                val heartbeatDAO = populateHeartBeatTable()

                //Act & Assert
                val heartbeatupdate = HeartBeat(id = 1, rate = 98, userId = 1)
                heartbeatDAO.updateByheartId(heartbeatupdate.id, heartbeatupdate)
                assertEquals(heartbeatupdate, heartbeatDAO.findByheartId(3))
            }
        }

        @Test
        fun `updating non-existant activity in table results in no updates`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val heartbeatDAO = populateHeartBeatTable()

                //Act & Assert
                val heartbeatupdate = HeartBeat(id = 1, rate = 98, userId = 1)
                heartbeatDAO.updateByheartId(4, heartbeatupdate)
                assertEquals(null, heartbeatDAO.findByheartId(4))
                assertEquals(5, heartbeatDAO.getAll().size)
            }
        }

        @Nested
        inner class DeleteActivities {
            @Test
            fun `deleting a non-existant activity (by id) in table results in no deletion`() {
                transaction {

                    //Arrange - create and populate tables with three users and three activities
                    val userDAO = populateUserTable()
                    val heartbeatDAO = populateHeartBeatTable()

                    //Act & Assert
                    assertEquals(5, heartbeatDAO.getAll().size)
                    heartbeatDAO.deleteByheartId(4)
                    assertEquals(5, heartbeatDAO.getAll().size)
                }
            }

            @Test
            fun `deleting an existing activity (by id) in table results in record being deleted`() {
                transaction {

                    //Arrange - create and populate tables with three users and three activities
                    val userDAO = populateUserTable()
                    val heartbeatDAO = populateHeartBeatTable()

                    //Act & Assert
                    assertEquals(5, heartbeatDAO.getAll().size)
                    heartbeatDAO.deleteByheartId(activity3.id)
                    assertEquals(3, heartbeatDAO.getAll().size)
                }
            }

            @Test
            fun `deleting activities when none exist for user id results in no deletion`() {
                transaction {

                    //Arrange - create and populate tables with three users and three activities
                    val userDAO = populateUserTable()
                    val heartbeatDAO = populateHeartBeatTable()

                    //Act & Assert
                    assertEquals(3, heartbeatDAO.getAll().size)
                    heartbeatDAO.deleteByUserId(3)
                    assertEquals(3, heartbeatDAO.getAll().size)
                }
            }



            @Test
            fun `deleting activities when 1 or more exist for user id results in deletion`() {
                transaction {

                    //Arrange - create and populate tables with three users and three activities
                    val userDAO = populateUserTable()
                    val heartbeatDAO = populateHeartBeatTable()

                    //Act & Assert
                    assertEquals(3, heartbeatDAO.getAll().size)
                    heartbeatDAO.deleteByUserId(1)
                    assertEquals(1, heartbeatDAO.getAll().size)
                }
            }
        }
    }





}