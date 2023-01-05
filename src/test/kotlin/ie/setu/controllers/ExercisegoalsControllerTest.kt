package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.Exercise_goals
import ie.setu.domain.User
import ie.setu.domain.db.Exercise_goal
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

class ExercisegoalsControllerTest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()


    //helper function to add a test user to the database
    private fun updateExercise_goals (id: Int, Calories_To_Burn: Int, Steps: Int, Date: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/exercisegoals/$id")
            .body("{\"Calories_To_Burn\":\"$Calories_To_Burn\", \"Steps\":\"$Steps\", \"Date\":\"$Date\", \"userId\":\"$userId\"}")
            .asJson()
    }


    @Nested
    inner class DeleteExercise_goals{

        @Test
        fun `deleting a Activity when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteExercise_goals(-1).status)
        }

        @Test
        fun `deleting a Activity when it exists, returns a 204 response`() {
            var id =1
            //Arrange - add the user that we plan to do a delete on
            val addedResponse = addExercise_goals(
                exerciseid, exercisecaloriestoburn, exercisesteps, exercisedatetime, exerciseuserid)

//            val addedGoal : Exercise_goals = jsonToObject(addedResponse.body.toString())

            //Act & Assert - delete the added user and assert a 204 is returned
            assertEquals(204, deleteExercise_goals(exerciseid).status)

            //Act & Assert - attempt to retrieve the deleted user --> 404 response
            assertEquals(404, retrieveExercise_goalsById(id).status)
        }

        @Test
        fun `updating a user when it doesn't exist, returns a 404 response`() {

            //Arrange - creating some text fixture data
//            val updatedName = "Updated Name"
//            val updatedEmail = "Updated Email"

            //Act & Assert - attempt to update the email and name of user that doesn't exist
            assertEquals(404, updateExercise_goals(updatedexerciseid, updatedexercisecaloriestoburn,
                updatedexercisesteps, updatedexercisedatetime, updatedexerciseuserid).status)
        }
    }


    @Nested
    inner class ReadUsers {

        @Test
        fun `get all Activities from the database returns 200 or 404 response`() {
            val response = Unirest.get(origin + "/api/Activities/").asString()
            if (response.status == 200) {
                val retrievedActivities: ArrayList<Exercise_goal> = jsonToObject(response.body.toString())
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
            val retrieveResponse = Unirest.get(origin + "/api/exercisegoals/${id}").asString()

            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }


        @Test
        fun `get user by email when user does not exist returns 404 response`() {
            // Arrange & Act - attempt to retrieve the non-existent user from the database

            val retrieveResponse = Unirest.get(origin + "/api/exercisegoals/discription/${nonExistingid}").asString()
            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }


        @Test
        fun `getting a user by id when id exists, returns a 200 response`() {

            //Arrange - add the user
            val addResponse = addExercise_goals(exerciseid, exercisecaloriestoburn, exercisesteps, exercisedatetime, exerciseuserid)
            val addexercise : Exercise_goals = jsonToObject(addResponse.body.toString())

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse = retrieveExercise_goalsById(addexercise.id)
            assertEquals(200, retrieveResponse.status)

            //After - restore the db to previous state by deleting the added user
            deleteExercise_goals(addexercise.id)
        }


        @Test
        fun `getting a Activity by email when email exists, returns a 200 response`() {

            //Arrange - add the Activity
            addExercise_goals(
                exerciseid, exercisecaloriestoburn, exercisesteps, exercisedatetime, exerciseuserid)

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse = retrieveExercise_goalsById(activityid)
            assertEquals(200, retrieveResponse.status)

            //After - restore the db to previous state by deleting the added user
            val retrievedUser : User = jsonToObject(retrieveResponse.body.toString())
            deleteExercise_goals(retrievedUser.id)
        }

    }


    private fun deleteExercise_goals (id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/exercisegoals/$id").asString()
    }

    private fun addExercise_goals (id:Int, calories: Int, steps: Int, datetime: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.post(origin + "/api/exercisegoals")
            .body("{\"id\":\"$id\",\"calories_to_burn\":\"$calories\", \"steps\":\"$steps\",\"date\":\"$datetime\", \"userId\":\"$userId\" }")
            .asJson()
    }

    private fun retrieveExercise_goalsById(id: Int) : HttpResponse<String> {
        return Unirest.get(origin + "/api/exercisegoals/${id}").asString()
    }

    @Test
    fun `add a Activity with correct details returns a 201 response`() {

        //Arrange & Act & Assert
        //    add the user and verify return code (using fixture data)
        val addResponse = addExercise_goals(exerciseid, exercisecaloriestoburn, exercisesteps, exercisedatetime, exerciseuserid)
        assertEquals(201, addResponse.status)

        //Assert - retrieve the added user from the database and verify return code
        val retrieveResponse= retrieveExercise_goalsById(exerciseid)
        assertEquals(200, retrieveResponse.status)

        //Assert - verify the contents of the retrieved user
        val retrievedExercise : Exercise_goals = jsonToObject(addResponse.body.toString())
        assertEquals(exercisecaloriestoburn, retrievedExercise.Calories_To_Burn)
        assertEquals(exerciseid, retrievedExercise.id)

        //After - restore the db to previous state by deleting the added user
        val deleteResponse = deleteExercise_goals(retrievedExercise.id)
        assertEquals(204, deleteResponse.status)
    }







}

