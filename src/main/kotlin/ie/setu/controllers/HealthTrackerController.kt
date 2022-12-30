package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.Activity
import ie.setu.domain.HeartBeat
import ie.setu.domain.User
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.HeartBeatDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*

object HealthTrackerController {

    private val userDao = UserDAO()
    private val activityDAO = ActivityDAO()
    private val heartBeatDAO = HeartBeatDAO()


    @OpenApi(
        summary = "Get all users",
        operationId = "getAllUsers",
        tags = ["User"],
        path = "/api/users",
        method = HttpMethod.GET,
        responses = [OpenApiResponse("200", [OpenApiContent(Array<User>::class)])]
    )
    fun getAllUsers(ctx: Context) {
        val users = userDao.getAll()
        if (users.size != 0) {
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(users)
    }

    @OpenApi(
        summary = "Get user by ID",
        operationId = "getUserById",
        tags = ["User"],
        path = "/api/users/{user-id}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("200", [OpenApiContent(User::class)])]
    )
    fun getUserByUserId(ctx: Context) {
        val user = userDao.findById(ctx.pathParam("user-id").toInt())
        if (user != null) {
            ctx.json(user)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Add User",
        operationId = "addUser",
        tags = ["User"],
        path = "/api/users",
        method = HttpMethod.POST,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("200")]
    )
    fun addUser(ctx: Context) {
        val user : User = jsonToObject(ctx.body())
        val userId = userDao.save(user)
        if (userId != null) {
            user.id = userId
            ctx.json(user)
            ctx.status(201)
        }
    }

    @OpenApi(
        summary = "Get user by Email",
        operationId = "getUserByEmail",
        tags = ["User"],
        path = "/api/users/email/{email}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("email", Int::class, "The user email")],
        responses  = [OpenApiResponse("200", [OpenApiContent(User::class)])]
    )

    fun getUserByEmail(ctx: Context) {
        val user = userDao.findByEmail(ctx.pathParam("email"))
        if (user != null) {
            ctx.json(user)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Delete user by ID",
        operationId = "deleteUserById",
        tags = ["User"],
        path = "/api/users/{user-id}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun deleteUser(ctx: Context){
        if (userDao.delete(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    @OpenApi(
        summary = "Update user by ID",
        operationId = "updateUserById",
        tags = ["User"],
        path = "/api/users/{user-id}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("user-id", Int::class, "The user ID")],
        responses  = [OpenApiResponse("204")]
    )
    fun updateUser(ctx: Context){
        val foundUser : User = jsonToObject(ctx.body())
        if ((userDao.update(id = ctx.pathParam("user-id").toInt(), user=foundUser)) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    //--------------------------------------------------------------
    // ActivityDAOI specifics
    //-------------------------------------------------------------

    fun getAllActivities(ctx: Context) {
        //mapper handles the deserialization of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        ctx.json(mapper.writeValueAsString( activityDAO.getAll() ))
    }

    fun getActivitiesByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val activities = activityDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (activities.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                ctx.json(mapper.writeValueAsString(activities))
            }
        }
    }

    fun addActivity(ctx: Context) {
        //mapper handles the serialisation of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val activity = mapper.readValue<Activity>(ctx.body())
        val returnid = activityDAO.save(activity)

        if (returnid != null) {
            activity.id = returnid
            ctx.json(activity)
            ctx.status(201)
        }
    }

    fun getActivitiesByActivityId(ctx: Context) {
        val activity = activityDAO.findByActivityId((ctx.pathParam("activity-id").toInt()))
        if (activity != null){
            val mapper = jacksonObjectMapper()
                .registerModule(JodaModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            ctx.json(mapper.writeValueAsString(activity))
        }
    }

    fun deleteActivityByActivityId(ctx: Context){
        if (activityDAO.deleteByActivityId(ctx.pathParam("activity-id").toInt()) != 0) {
            ctx.status(204)
        }
        else{
            ctx.status(404)
        }
    }


    fun deleteActivityByUserId(ctx: Context){
        activityDAO.deleteByUserId(ctx.pathParam("user-id").toInt())
    }

    fun updateActivity(ctx: Context){
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val activity = mapper.readValue<Activity>(ctx.body())
        activityDAO.updateByActivityId(
            activityId = ctx.pathParam("activity-id").toInt(),
            activityDTO=activity)
    }


    // heart rate stuff

    @OpenApi(
        summary = "Get all heart beat",
        operationId = "getAllHeartBeat",
        tags = ["HeartBeat"],
        path = "/api/heartbeats",
        method = HttpMethod.GET,
        responses = [OpenApiResponse("200", [OpenApiContent(Array<HeartBeat>::class)])]
    )
    fun getAllHeartBeat(ctx: Context) {
        val beat = heartBeatDAO.getAll()
        if (beat.size != 0) {
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(beat)
    }

    @OpenApi(
        summary = "Get beat by ID",
        operationId = "getBeatByID",
        tags = ["HeartBeat"],
        path = "/api/heartbeats/{heart-rate}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("heart-rate", Int::class, "heart rate")],
        responses  = [OpenApiResponse("200", [OpenApiContent(HeartBeat::class)])]
    )
    fun getBeatByID(ctx: Context) {
        val beat = heartBeatDAO.findByheartId(ctx.pathParam("heart-rate").toInt())
        if (beat != null) {
            ctx.json(beat)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    @OpenApi(
        summary = "Add beat",
        operationId = "addBeat",
        tags = ["HeartBeat"],
        path = "/api/heartbeats",
        method = HttpMethod.POST,
        pathParams = [OpenApiParam("heart-rate", Int::class, "heart rate")],
        responses  = [OpenApiResponse("200")]
    )
    fun addBeat(ctx: Context) {
        val beat : HeartBeat = jsonToObject(ctx.body())
        val beat1 = heartBeatDAO.save(beat)
        if (beat1 != null) {
            beat.id = beat1
            ctx.json(beat1)
            ctx.status(201)
        }
    }



    @OpenApi(
        summary = "Delete beat by ID",
        operationId = "deleteBeatById",
        tags = ["HeartBeat"],
        path = "/api/heartbeats/{heart-rate}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("heart-rate", Int::class, "heart rate")],
        responses  = [OpenApiResponse("204")]
    )
    fun deleteBeatById(ctx: Context){
        if (heartBeatDAO.deleteByheartId(ctx.pathParam("heart-rate").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    @OpenApi(
        summary = "Update beat by ID",
        operationId = "updateBeatById",
        tags = ["HeartBeat"],
        path = "/api/heartbeats/{heart-rate}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("heart-rate", Int::class, "heart rate")],
        responses  = [OpenApiResponse("204")]
    )
    fun updateBeatById(ctx: Context){
        val foundBeat : HeartBeat = jsonToObject(ctx.body())
        if (heartBeatDAO.updateByheartId(heartId = ctx.pathParam("heart-rate").toInt(),heartDTO = foundBeat ) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

}
