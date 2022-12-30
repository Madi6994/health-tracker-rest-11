package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.Activity
import ie.setu.domain.Tracking_Water_Intake
import ie.setu.domain.User
import ie.setu.helpers.*
import ie.setu.utils.jsonToObject
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.joda.time.DateTime
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TrackingwaterintakeControllerTest {



    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()


    //helper function to add a test user to the database
    private fun updateTrackinwaterintake (id: Int, Glass_of_Water: Int, DateTime: DateTime, UserID: Int): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/trackingwaterintake/$id")
            .body("{\"Glass_of_Water\":\"$Glass_of_Water\", \"DateTime\":\"$DateTime\",\"UserID\":\"$UserID\" }")
            .asJson()
    }

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















    private fun deleteTrackingwaterintake (id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/trackingwaterintake/$id").asString()
    }

    private fun addTrackingwaterintake (id:Int ,glassofwater: Int, datetime: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.post(origin + "/api/trackingwaterintake")
            .body("{\"glassofwater\":\"$glassofwater\", \"datetime\":\"$datetime\",\"userId\":\"$userId\"}")
            .asJson()
    }

    private fun retrieveTrackingwaterintake(id: Int) : HttpResponse<String> {
        return Unirest.get(origin + "/api/trackingwaterintake/${id}").asString()
    }


    @Test
    fun `add a Activity with correct details returns a 201 response`() {

        //Arrange & Act & Assert
        //    add the user and verify return code (using fixture data)
        val addResponse = addTrackingwaterintake(
            waterintakeid, waterintakeglassofwater, waterintakedatetime, waterintakeuserid
        )
        assertEquals(201, addResponse.status)

        //Assert - retrieve the added user from the database and verify return code
        val retrieveResponse= retrieveTrackingwaterintake(waterintakeid)
        assertEquals(200, retrieveResponse.status)

        //Assert - verify the contents of the retrieved user
        val retrievedTrackingwaterintake : Tracking_Water_Intake = jsonToObject(addResponse.body.toString())
        assertEquals(waterintakeglassofwater, retrievedTrackingwaterintake.Glass_of_Water)
        assertEquals(activityid, retrievedTrackingwaterintake.ID)

        //After - restore the db to previous state by deleting the added user
        val deleteResponse = deleteTrackingwaterintake(retrievedTrackingwaterintake.ID)
        assertEquals(204, deleteResponse.status)
    }




}