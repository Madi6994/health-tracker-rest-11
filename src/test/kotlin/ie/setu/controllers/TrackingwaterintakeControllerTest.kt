package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.helpers.ServerContainer
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.joda.time.DateTime

class TrackingwaterintakeControllerTest {



    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()


    //helper function to add a test user to the database
    private fun updateUser (id: Int, Glass_of_Water: Int, DateTime: DateTime, UserID: Int): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/users/$id")
            .body("{\"Glass_of_Water\":\"$Glass_of_Water\", \"DateTime\":\"$DateTime\",\"UserID\":\"$UserID\" }")
            .asJson()
    }




}