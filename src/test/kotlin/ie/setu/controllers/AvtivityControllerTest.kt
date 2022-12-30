package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.controllers.HealthTrackerController.deleteActivityByActivityId
import ie.setu.domain.Activity
import ie.setu.domain.User
import ie.setu.domain.db.Activities
import ie.setu.helpers.*
import ie.setu.utils.jsonToObject
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.joda.time.DateTime
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class AvtivityControllerTest {


    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()


    //helper function to add a test user to the database
    private fun updateActivity (id: Int, description: String, duration: Double, calories: Int, started: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/Activity/$id")
            .body("{\"description\":\"$description\", \"duration\":\"$duration\",\"calories\":\"$calories\", \"started\":\"$started\", \"userId\":\"$userId\" }")
            .asJson()
    }

    @Nested
    inner class DeleteActivity{

        @Test
        fun `deleting a Activity when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteActivity(-1).status)
        }

        @Test
        fun `deleting a Activity when it exists, returns a 204 response`() {
        var id =1
            //Arrange - add the user that we plan to do a delete on
            val addedResponse = addActivity(activityid, activitydiscription, activityduration, activitycalories, activitydatetime,
                activityuserid)
            val addedUser : User = jsonToObject(addedResponse.body.toString())

            //Act & Assert - delete the added user and assert a 204 is returned
            assertEquals(204, deleteActivity(id).status)

            //Act & Assert - attempt to retrieve the deleted user --> 404 response
            assertEquals(404, retrieveActivityById(id).status)
        }

        @Test
        fun `updating a user when it doesn't exist, returns a 404 response`() {

            //Arrange - creating some text fixture data
//            val updatedName = "Updated Name"
//            val updatedEmail = "Updated Email"

            //Act & Assert - attempt to update the email and name of user that doesn't exist
            assertEquals(404, updateActivity(updatedactivityid, updatedactivitydiscription,
                updatedactivityduration, updatedactivitycalories, updatedactivitydatetime, updatedactivityuserid).status)
        }
    }


    @Nested
    inner class ReadUsers {
        @Test
        fun `get all Activities from the database returns 200 or 404 response`() {
            val response = Unirest.get(origin + "/api/activities/").asString()
            if (response.status == 200) {
                val retrievedActivities: ArrayList<Activities> = jsonToObject(response.body.toString())
                assertNotEquals(0, retrievedActivities.size)
            }
            else {
                assertEquals(404, response.status)
            }
        }


        @Test
        fun `get user by id when Activities does not exist returns 404 response`() {

            //Arrange - test data for user id
            val id = Integer.MIN_VALUE

            // Act - attempt to retrieve the non-existent user from the database
            val retrieveResponse = Unirest.get(origin + "/api/activities/${id}").asString()

            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `get user by email when user does not exist returns 404 response`() {
            // Arrange & Act - attempt to retrieve the non-existent user from the database

            val retrieveResponse = Unirest.get(origin + "/api/activities/discription/${nonExistingdiscription}").asString()
            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `getting a user by id when id exists, returns a 200 response`() {

            //Arrange - add the user
            val addResponse = addActivity(activityid, activitydiscription, activityduration, activitycalories, activitydatetime,
                activityuserid )
            val addedUser : User = jsonToObject(addResponse.body.toString())

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse = retrieveActivityById(addedUser.id)
            assertEquals(200, retrieveResponse.status)

            //After - restore the db to previous state by deleting the added user
            deleteActivity(addedUser.id)
        }

        @Test
        fun `getting a Activity by email when email exists, returns a 200 response`() {

            //Arrange - add the Activity
            addActivity(activityid, activitydiscription, activityduration, activitycalories, activitydatetime,
                activityuserid )

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse = retrieveActivityById(activityid)
            assertEquals(200, retrieveResponse.status)

            //After - restore the db to previous state by deleting the added user
            val retrievedUser : User = jsonToObject(retrieveResponse.body.toString())
            deleteActivity(retrievedUser.id)
        }


    }

    private fun deleteActivity (id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/activities/$id").asString()
    }

    private fun addActivity (id:Int, description: String, duration: Double, calories: Int, started: DateTime, userid: Int): HttpResponse<JsonNode> {
        return Unirest.post(origin + "/api/activities")
            .body("{\"id\":\"$id\",\"description\":\"$description\", \"duration\":\"$duration\",\"calories\":\"$calories\", \"started\":\"$started\", \"userId\":\"$userid\" }")
            .asJson()
    }



    private fun retrieveActivityById(id: Int) : HttpResponse<String> {
        return Unirest.get(origin + "/api/activities/${id}").asString()
    }





    @Test
    fun `add a Activity with correct details returns a 201 response`() {

        //Arrange & Act & Assert
        //    add the user and verify return code (using fixture data)
        val addResponse = addActivity(activityid, activitydiscription, activityduration, activitycalories, activitydatetime,
            activityuserid)
        assertEquals(201, addResponse.status)

        //Assert - retrieve the added user from the database and verify return code
        val retrieveResponse= retrieveActivityById(activityid)
        assertEquals(200, retrieveResponse.status)

        //Assert - verify the contents of the retrieved user
        val retrievedActivity : Activity = jsonToObject(addResponse.body.toString())
        assertEquals(activitydiscription, retrievedActivity.description)

        //After - restore the db to previous state by deleting the added user
        val deleteResponse = deleteActivity(retrievedActivity.id)
        assertEquals(204, deleteResponse.status)
    }












}