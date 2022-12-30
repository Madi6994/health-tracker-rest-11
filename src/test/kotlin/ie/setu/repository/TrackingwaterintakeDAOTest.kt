package ie.setu.repository

import ie.setu.domain.Tracking_Water_Intake
import ie.setu.domain.db.Tracking_water_intake
import ie.setu.domain.repository.TrackingwaterintakeDAO
import ie.setu.helpers.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


private val activity3 = activities.get(2)
class TrackingwaterintakeDAOTest {

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
                val waterintakeDAO = populateWater_IntakeTable()
                //Act & Assert
                assertEquals(3, waterintakeDAO.getAll().size)
                assertEquals(Waterintake.get(0), waterintakeDAO.findBywaterintakeId(Waterintake.get(0).ID))
                assertEquals(Waterintake.get(1), waterintakeDAO.findBywaterintakeId(Waterintake.get(1).ID))
                assertEquals(Waterintake.get(2), waterintakeDAO.findBywaterintakeId(Waterintake.get(2).ID))
                assertEquals(Waterintake.get(3), waterintakeDAO.findBywaterintakeId(Waterintake.get(3).ID))
                assertEquals(Waterintake.get(4), waterintakeDAO.findBywaterintakeId(Waterintake.get(4).ID))
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
                val waterintakeDAO = populateWater_IntakeTable()
                //Act & Assert
                assertEquals(3, waterintakeDAO.getAll().size)
            }
        }


        @Test
        fun `get activity by user id that has no activities, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val waterintakeDAO = populateWater_IntakeTable()
                //Act & Assert
                assertEquals(0, waterintakeDAO.findByUserId(3).size)
            }
        }


        @Test
        fun `get activity by user id that exists, results in a correct activitie(s) returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val waterintakeDAO = populateWater_IntakeTable()
                //Act & Assert
                assertEquals(Waterintake.get(0), waterintakeDAO.findByUserId(1).get(0))
                assertEquals(Waterintake.get(1), waterintakeDAO.findByUserId(1).get(1))
                assertEquals(Waterintake.get(2), waterintakeDAO.findByUserId(2).get(0))
                assertEquals(Waterintake.get(3), waterintakeDAO.findByUserId(1).get(1))
                assertEquals(Waterintake.get(4), waterintakeDAO.findByUserId(1).get(0))
            }
        }


        @Test
        fun `get all activities over empty table returns none`() {
            transaction {

                //Arrange - create and setup activityDAO object
                SchemaUtils.create(Tracking_water_intake)
                val waterintakeDAO = TrackingwaterintakeDAO()

                //Act & Assert
                assertEquals(0, waterintakeDAO.getAll().size)
            }
        }


        @Test
        fun `get activity by activity id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val waterintakeDAO = populateWater_IntakeTable()
                //Act & Assert
                assertEquals(null, waterintakeDAO.findBywaterintakeId(4))
            }
        }

        @Test
        fun `get activity by activity id that exists, results in a correct activity returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val waterintakeDAO = populateWater_IntakeTable()
                //Act & Assert
                assertEquals(Waterintake.get(1), waterintakeDAO.findBywaterintakeId(1))
                assertEquals(Waterintake.get(3), waterintakeDAO.findBywaterintakeId(3))
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
                val waterintakeDAO = populateWater_IntakeTable()

                //Act & Assert
                val Waterintakeupdate = Tracking_Water_Intake(ID = 1, Glass_of_Water = 4, DateTime = DateTime.now(), UserID = 1)
                waterintakeDAO.updateBywaterintakeId(Waterintakeupdate.ID, Waterintakeupdate)
                assertEquals(Waterintakeupdate, waterintakeDAO.findBywaterintakeId(3))
            }
        }

        @Test
        fun `updating non-existant activity in table results in no updates`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val waterintakeDAO = populateWater_IntakeTable()

                //Act & Assert
                val Waterintakeupdate = Tracking_Water_Intake(ID = 1, Glass_of_Water = 4, DateTime = DateTime.now(), UserID = 1)
                waterintakeDAO.updateBywaterintakeId(4, Waterintakeupdate)
                assertEquals(null, waterintakeDAO.findBywaterintakeId(4))
                assertEquals(5, waterintakeDAO.getAll().size)
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
                val waterintakeDAO = populateWater_IntakeTable()

                //Act & Assert
                assertEquals(5, waterintakeDAO.getAll().size)
                waterintakeDAO.deleteBywaterintakeId(4)
                assertEquals(5, waterintakeDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing activity (by id) in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val waterintakeDAO = populateWater_IntakeTable()

                //Act & Assert
                assertEquals(5, waterintakeDAO.getAll().size)
                waterintakeDAO.deleteBywaterintakeId(activity3.id)
                assertEquals(3, waterintakeDAO.getAll().size)
            }
        }

        @Test
        fun `deleting activities when none exist for user id results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val waterintakeDAO = populateWater_IntakeTable()

                //Act & Assert
                assertEquals(3, waterintakeDAO.getAll().size)
                waterintakeDAO.deleteByUserId(3)
                assertEquals(3, waterintakeDAO.getAll().size)
            }
        }



        @Test
        fun `deleting activities when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val waterintakeDAO = populateWater_IntakeTable()

                //Act & Assert
                assertEquals(3, waterintakeDAO.getAll().size)
                waterintakeDAO.deleteByUserId(1)
                assertEquals(1, waterintakeDAO.getAll().size)
            }
        }
    }










}