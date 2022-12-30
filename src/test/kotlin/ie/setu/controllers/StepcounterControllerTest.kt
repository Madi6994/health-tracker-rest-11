package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.Activity
import ie.setu.domain.Step_Counter
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

class StepcounterControllerTest {




    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()


    //helper function to add a test user to the database
    private fun updateStepCounter (id: Int, Daily_Steps: Int, UserID: Int): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/stepcounter/$id")
            .body("{\"Daily_Steps\":\"$Daily_Steps\", \"UserID\":\"$UserID\"}")
            .asJson()
    }

    @Nested
    inner class DeleteActivity{

        @Test
        fun `deleting a Activity when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteStepCounter(-1).status)
        }

        @Test
        fun `deleting a Activity when it exists, returns a 204 response`() {
            var id =1
            //Arrange - add the user that we plan to do a delete on
            val addedResponse = addStepCounter(stepcounterid, stepcounterdailysteps, stepcounteruserid)
            val addedUser : User = jsonToObject(addedResponse.body.toString())

            //Act & Assert - delete the added user and assert a 204 is returned
            assertEquals(204, deleteStepCounter(id).status)

            //Act & Assert - attempt to retrieve the deleted user --> 404 response
            assertEquals(404, retrieveStepCounterById(id).status)
        }

        @Test
        fun `updating a user when it doesn't exist, returns a 404 response`() {

            //Arrange - creating some text fixture data
//            val updatedName = "Updated Name"
//            val updatedEmail = "Updated Email"

            //Act & Assert - attempt to update the email and name of user that doesn't exist
            assertEquals(404, updateStepCounter(updatestepcounterid, updatestepcounterdailysteps,
                updatestepcounteruserid).status)
        }
    }


    @Nested
    inner class ReadUsers {
        @Test
        fun `get all Activities from the database returns 200 or 404 response`() {
            val response = Unirest.get(origin + "/api/stepcounter/").asString()
            if (response.status == 200) {
                val retrievedstepcounter: ArrayList<Step_Counter> = jsonToObject(response.body.toString())
                assertNotEquals(0, retrievedstepcounter.size)
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
            val retrieveResponse = Unirest.get(origin + "/api/stepcounter/${id}").asString()

            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `get user by email when user does not exist returns 404 response`() {
            // Arrange & Act - attempt to retrieve the non-existent user from the database

            val retrieveResponse = Unirest.get(origin + "/api/stepcounter/dailysteps/${nonExistingdailysteps}").asString()
            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `getting a user by id when id exists, returns a 200 response`() {

            //Arrange - add the user
            val addResponse = addStepCounter(stepcounterid, stepcounterdailysteps, stepcounteruserid )
            val addedUser : User = jsonToObject(addResponse.body.toString())

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse = retrieveStepCounterById(addedUser.id)
            assertEquals(200, retrieveResponse.status)

            //After - restore the db to previous state by deleting the added user
            deleteStepCounter(addedUser.id)
        }

        @Test
        fun `getting a Activity by email when email exists, returns a 200 response`() {

            //Arrange - add the Activity
            addStepCounter(stepcounterid, stepcounterdailysteps, stepcounteruserid  )

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse = retrieveStepCounterById(stepcounterid)
            assertEquals(200, retrieveResponse.status)

            //After - restore the db to previous state by deleting the added user
            val retrievedUser : User = jsonToObject(retrieveResponse.body.toString())
            deleteStepCounter(retrievedUser.id)
        }


    }







    private fun deleteStepCounter (id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/stepcounter/$id").asString()
    }

    private fun addStepCounter (id:Int, counterdailystep: Int, userId: Int): HttpResponse<JsonNode> {
        return Unirest.post(origin + "/api/stepcounter")
            .body("{\"counterdailystep\":\"$counterdailystep\", \"userId\":\"$userId\" }")
            .asJson()
    }

    private fun retrieveStepCounterById(id: Int) : HttpResponse<String> {
        return Unirest.get(origin + "/api/stepcounter/${id}").asString()
    }





    @Test
    fun `add a Activity with correct details returns a 201 response`() {

        //Arrange & Act & Assert
        //    add the user and verify return code (using fixture data)
        val addResponse = addStepCounter(
            stepcounterid, stepcounterdailysteps, stepcounteruserid)
        assertEquals(201, addResponse.status)

        //Assert - retrieve the added user from the database and verify return code
        val retrieveResponse= retrieveStepCounterById(stepcounterid)
        assertEquals(200, retrieveResponse.status)

        //Assert - verify the contents of the retrieved user
        val retrievedstepcounter : Step_Counter = jsonToObject(addResponse.body.toString())
        assertEquals(stepcounterdailysteps, retrievedstepcounter.Daily_Steps)
        assertEquals(stepcounterid, retrievedstepcounter.ID)

        //After - restore the db to previous state by deleting the added user
        val deleteResponse = deleteStepCounter(retrievedstepcounter.ID)
        assertEquals(204, deleteResponse.status)
    }
}