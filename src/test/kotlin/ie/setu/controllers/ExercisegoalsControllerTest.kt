package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.User
import ie.setu.helpers.ServerContainer
import ie.setu.utils.jsonToObject
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.joda.time.DateTime
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ExercisegoalsControllerTest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()


    //helper function to add a test user to the database
    private fun updateExercise_goals (id: Int, Calories_To_Burn: Int, Steps: Int, Date: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/Exercise_goals/$id")
            .body("{\"Calories_To_Burn\":\"$Calories_To_Burn\", \"Steps\":\"$Steps\", \"Date\":\"$Date\", \"userId\":\"$userId\"}")
            .asJson()
    }


    @Nested
    inner class DeleteExercise_goals{

        @Test
        fun `deleting a Exercise_goals when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteExercise_goals(-1).status)
        }

        @Test
        fun `deleting a Exercise_goals when it exists, returns a 204 response`() {

            //Arrange - add the user that we plan to do a delete on
            val addedResponse = addExercise_goals("Running",23.0,220, DateTime.now(),4)
            val addedUser : User = jsonToObject(addedResponse.body.toString())

            //Act & Assert - delete the added user and assert a 204 is returned
            assertEquals(204, deleteExercise_goals(addedUser.id).status)

            //Act & Assert - attempt to retrieve the deleted user --> 404 response
            assertEquals(404, retrieveExercise_goalsById(addedUser.id).status)
        }
    }

    private fun deleteExercise_goals (id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/Activities/$id").asString()
    }

    private fun addExercise_goals (description: String, duration: Double, calories: Int, started: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.post(origin + "/api/Activities")
            .body("{\"description\":\"$description\", \"duration\":\"$duration\",\"calories\":\"$calories\", \"started\":\"$started\", \"userId\":\"$userId\" }")
            .asJson()
    }

    private fun retrieveExercise_goalsById(id: Int) : HttpResponse<String> {
        return Unirest.get(origin + "/api/Activities/${id}").asString()
    }









}