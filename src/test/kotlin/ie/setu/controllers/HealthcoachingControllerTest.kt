package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.Activity
import ie.setu.domain.Health_Coaching
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

class HealthcoachingControllerTest {



    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()


    //helper function to add a test user to the database
    private fun updateHealth_Coaching (id: Int, Protein_Intake: Int, macro_percentage: Int, UserID: Int): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/users/$id")
            .body("{\"Protein_Intake\":\"$Protein_Intake\", \"macro_percentage\":\"$macro_percentage\",\"UserID\":\"$UserID\" }")
            .asJson()
    }

    @Nested
    inner class DeleteActivity{

        @Test
        fun `deleting a Activity when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteHealth_Coaching(-1).status)
        }

        @Test
        fun `deleting a Activity when it exists, returns a 204 response`() {
            var id =5
            //Arrange - add the user that we plan to do a delete on
            val addedResponse = addHealth_Coaching(
                coachingid, coachingproteinintake, coachingmacropercentage, coachinguserid
            )
            val addedUser : User = jsonToObject(addedResponse.body.toString())

            //Act & Assert - delete the added user and assert a 204 is returned
            assertEquals(204, deleteHealth_Coaching(id).status)

            //Act & Assert - attempt to retrieve the deleted user --> 404 response
            assertEquals(404, retrieveHealth_CoachingById(id).status)
        }

        @Test
        fun `updating a user when it doesn't exist, returns a 404 response`() {

            //Arrange - creating some text fixture data
//            val updatedName = "Updated Name"
//            val updatedEmail = "Updated Email"

            //Act & Assert - attempt to update the email and name of user that doesn't exist
            assertEquals(404, updateHealth_Coaching(updatedcoachingid,updatedcoachingproteinintake,updatedcoachingmacropercentage,updatedcoachinguserid).status)
        }
    }


    @Nested
    inner class ReadUsers {
        @Test
        fun `get all Health_Coaching from the database returns 200 or 404 response`() {
            val response = Unirest.get(origin + "/api/healthcoaching/").asString()
            if (response.status == 200) {
                val retrievehealthcoaching: ArrayList<Health_Coaching> = jsonToObject(response.body.toString())
                assertNotEquals(0, retrievehealthcoaching.size)
            }
            else {
                assertEquals(404, response.status)
            }
        }

        @Test
        fun `get user by id when Health_coaching does not exist returns 404 response`() {

            //Arrange - test data for user id
            val id = Integer.MIN_VALUE

            // Act - attempt to retrieve the non-existent user from the database
            val retrieveResponse = Unirest.get(origin + "/api/healthcoaching/${id}").asString()

            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `get user by id when user does not exist returns 404 response`() {
            // Arrange & Act - attempt to retrieve the non-existent user from the database

            val retrieveResponse = Unirest.get(origin + "/api/healthcoaching/id/${nonExistingid}").asString()
            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `getting a user by id when id exists, returns a 200 response`() {

            //Arrange - add the user
            val addResponse = addHealth_Coaching(coachingid, coachingproteinintake, coachingmacropercentage, coachinguserid )
            val addedUser : User = jsonToObject(addResponse.body.toString())

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse = retrieveHealth_CoachingById(addedUser.id)
            assertEquals(200, retrieveResponse.status)

            //After - restore the db to previous state by deleting the added user
            deleteHealth_Coaching(addedUser.id)
        }

        @Test
        fun `getting a Activity by email when email exists, returns a 200 response`() {

            //Arrange - add the Activity
            addHealth_Coaching(coachingid, coachingproteinintake, coachingmacropercentage, coachinguserid )

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse = retrieveHealth_CoachingById(activityid)
            assertEquals(200, retrieveResponse.status)

            //After - restore the db to previous state by deleting the added user
            val retrievedUser : User = jsonToObject(retrieveResponse.body.toString())
            deleteHealth_Coaching(retrievedUser.id)
        }

    }































    private fun deleteHealth_Coaching (id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/healthcoaching/$id").asString()
    }

    private fun addHealth_Coaching (id:Int, protein: Int, macropercentage: Int,  userId: Int): HttpResponse<JsonNode> {
        return Unirest.post(origin + "/api/healthcoaching")
            .body("{\"protein\":\"$protein\", \"macropercentage\":\"$macropercentage\", \"userId\":\"$userId\" }")
            .asJson()
    }

    private fun retrieveHealth_CoachingById(id: Int) : HttpResponse<String> {
        return Unirest.get(origin + "/api/healthcoaching/${id}").asString()
    }





    @Test
    fun `add a Activity with correct details returns a 201 response`() {

        //Arrange & Act & Assert
        //    add the user and verify return code (using fixture data)
        val addResponse = addHealth_Coaching(
            coachingid, coachingproteinintake, coachingmacropercentage, coachinguserid)
        assertEquals(201, addResponse.status)

        //Assert - retrieve the added user from the database and verify return code
        val retrieveResponse= retrieveHealth_CoachingById(coachingid)
        assertEquals(200, retrieveResponse.status)

        //Assert - verify the contents of the retrieved user
        val retrievedCoaching : Health_Coaching = jsonToObject(addResponse.body.toString())
        assertEquals(coachingproteinintake, retrievedCoaching.Protein_Intake)
        assertEquals(coachingid, retrievedCoaching.ID)

        //After - restore the db to previous state by deleting the added user
        val deleteResponse = deleteHealth_Coaching(retrievedCoaching.ID)
        assertEquals(204, deleteResponse.status)
    }
}