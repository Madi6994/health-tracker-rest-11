package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.helpers.ServerContainer
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.joda.time.DateTime

class ExercisegoalsControllerTest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()


    //helper function to add a test user to the database
    private fun updateUser (id: Int, Calories_To_Burn: Int, Steps: Int, Date: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/Exercise_goals/$id")
            .body("{\"Calories_To_Burn\":\"$Calories_To_Burn\", \"Steps\":\"$Steps\", \"Date\":\"$Date\", \"userId\":\"$userId\"}")
            .asJson()
    }
}