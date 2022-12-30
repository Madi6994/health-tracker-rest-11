package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.helpers.ServerContainer
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.joda.time.DateTime

class AvtivityControllerTest {


    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()


    //helper function to add a test user to the database
    private fun updateUser (id: Int, description: String, duration: Double, calories: Int, started: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/Activity/$id")
            .body("{\"description\":\"$description\", \"duration\":\"$duration\",\"calories\":\"$calories\", \"started\":\"$started\", \"userId\":\"$userId\" }")
            .asJson()
    }













}