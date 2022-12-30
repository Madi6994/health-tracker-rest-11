package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.helpers.ServerContainer
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest

class StepcounterControllerTest {




    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()


    //helper function to add a test user to the database
    private fun updateUser (id: Int, Daily_Steps: Int, UserID: Int): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/users/$id")
            .body("{\"Daily_Steps\":\"$Daily_Steps\", \"UserID\":\"$UserID\"}")
            .asJson()
    }

}