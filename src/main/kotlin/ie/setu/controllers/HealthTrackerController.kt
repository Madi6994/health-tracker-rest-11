package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.*
import ie.setu.domain.repository.*
import ie.setu.utils.jsonToObject
import io.javalin.http.Context
import io.javalin.plugin.openapi.annotations.*

object HealthTrackerController {

    private val userDao = UserDAO()
    private val activityDAO = ActivityDAO()
    private val heartBeatDAO = HeartBeatDAO()
    private val exercisegoalsDAO = ExercisegoalsDAO()
    private val healthDAO = HealthcoachingDAO()
    private val counterDAO = StepcounterDAO()
    private val waterDAO = TrackingwaterintakeDAO()



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




    // Exercise Goal stuff


    @OpenApi(
        summary = "Get all heart beat",
        operationId = "getallgoals",
        tags = ["Exercise_goals"],
        path = "/api/exercisegoals",
        method = HttpMethod.GET,
        responses = [OpenApiResponse("200", [OpenApiContent(Array<Exercise_goals>::class)])]
    )
    fun getallgoals(ctx: Context) {
        val goal = exercisegoalsDAO.getAll()
        if (goal.size!=0) {
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(goal)
    }

    @OpenApi(
        summary = "Get goal by ID",
        operationId = "getgoalByID",
        tags = ["Exercise_goals"],
        path = "/api/exercisegoals/{exercise-goal}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("exercise-goal", Int::class, "exercise goal")],
        responses  = [OpenApiResponse("200", [OpenApiContent(Exercise_goals::class)])]
    )
    fun getgoalByID(ctx: Context) {
        val goal = exercisegoalsDAO.findByexerciseId(ctx.pathParam("exercise-goal").toInt())
        if (goal != null) {
            ctx.json(goal)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }


    @OpenApi(
        summary = "Add goal",
        operationId = "addGoal",
        tags = ["Exercise_goals"],
        path = "/api/exercisegoals",
        method = HttpMethod.POST,
        pathParams = [OpenApiParam("exercise-goal", Int::class, "exercise goal")],
        responses  = [OpenApiResponse("200")]
    )
    fun addGoal(ctx: Context) {
        val goal : Exercise_goals = jsonToObject(ctx.body())
        val goal1 = exercisegoalsDAO.save(goal)
        if (goal1 != null) {
            goal.id = goal1
            ctx.json(goal1)
            ctx.status(201)
        }
    }



    @OpenApi(
        summary = "Delete goal by ID",
        operationId = "deleteGoalById",
        tags = ["Exercise_goals"],
        path = "/api/exercisegoals/{exercise-goal}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("exercise-goal", Int::class, "exercise goal")],
        responses  = [OpenApiResponse("204")]
    )
    fun deleteGoalById(ctx: Context){
        if (exercisegoalsDAO.deleteByexerciseId(ctx.pathParam("exercise-goal").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }


    @OpenApi(
        summary = "Update goal by ID",
        operationId = "updateGoalById",
        tags = ["exercisegoals"],
        path = "/api/exercisegoals/{exercise-goal}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("exercise-goal", Int::class, "exercise goal")],
        responses  = [OpenApiResponse("204")]
    )
    fun updateGoalById(ctx: Context){
        val foundGoal : Exercise_goals = jsonToObject(ctx.body())
        if (exercisegoalsDAO.updateByexerciseId(exercId = ctx.pathParam("heart-rate").toInt(),exercDTO = foundGoal ) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }


    // Health Coaching stuff

    @OpenApi(
        summary = "Get all health coaching",
        operationId = "getAllHealthCoaching",
        tags = ["HealthCoaching"],
        path = "/api/healthcoaching",
        method = HttpMethod.GET,
        responses = [OpenApiResponse("200", [OpenApiContent(Array<Health_Coaching>::class)])]
    )
    fun getAllHealthCoaching(ctx: Context) {
        val coaching = healthDAO.getAll()
        if (coaching.size != 0) {
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(coaching)
    }



    @OpenApi(
        summary = "Get coaching by ID",
        operationId = "getCoachingByID",
        tags = ["HealthCoaching"],
        path = "/api/healthcoaching/{health-coaching}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("health-coaching", Int::class, "health coaching")],
        responses  = [OpenApiResponse("200", [OpenApiContent(Health_Coaching::class)])]
    )
    fun getCoachingByID(ctx: Context) {
        val coaching = healthDAO.findBycoachingId(ctx.pathParam("health-coaching").toInt())
        if (coaching != null) {
            ctx.json(coaching)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }



    @OpenApi(
        summary = "Add coaching",
        operationId = "addcoaching",
        tags = ["HealthCoaching"],
        path = "/api/healthcoaching",
        method = HttpMethod.POST,
        pathParams = [OpenApiParam("health-coaching", Int::class, "health coaching")],
        responses  = [OpenApiResponse("200")]
    )
    fun addcoaching(ctx: Context) {
        val coaching : Health_Coaching = jsonToObject(ctx.body())
        val coaching1 = healthDAO.save(coaching)
        if (coaching1 != null) {
            coaching.ID = coaching1
            ctx.json(coaching1)
            ctx.status(201)
        }
    }


    @OpenApi(
        summary = "Delete coaching by ID",
        operationId = "deleteCoachingById",
        tags = ["HealthCoaching"],
        path = "/api/healthcoaching/{health-coaching}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("health-coaching", Int::class, "health coaching")],
        responses  = [OpenApiResponse("204")]
    )
    fun deleteCoachingById(ctx: Context){
        if (healthDAO.deleteBycoachId(ctx.pathParam("health-coaching").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }


    @OpenApi(
        summary = "Update coaching by ID",
        operationId = "updateCoachingById",
        tags = ["HealthCoaching"],
        path = "/api/healthcoaching/{health-coaching}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("health-coaching", Int::class, "health coaching")],
        responses  = [OpenApiResponse("204")]
    )
    fun updateCoachingById(ctx: Context){
        val foundCoaching : Health_Coaching = jsonToObject(ctx.body())
        if (healthDAO.updateBycoachId(coachId = ctx.pathParam("health-coaching").toInt(),coachDTO = foundCoaching ) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }


    // Step Counter stuff

    @OpenApi(
        summary = "Get all step counter",
        operationId = "getAllStepCounter",
        tags = ["StepCounter"],
        path = "/api/stepcounter",
        method = HttpMethod.GET,
        responses = [OpenApiResponse("200", [OpenApiContent(Array<Step_Counter>::class)])]
    )
    fun getAllStepCounter(ctx: Context) {
        val counter = counterDAO.getAll()
        if (counter.size != 0) {
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(counter)
    }



    @OpenApi(
        summary = "Get step counter by ID",
        operationId = "getCounterByID",
        tags = ["StepCounter"],
        path = "/api/stepcounter/{step-counter}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("step-counter", Int::class, "step counter")],
        responses  = [OpenApiResponse("200", [OpenApiContent(Step_Counter::class)])]
    )
    fun getCounterByID(ctx: Context) {
        val counter = counterDAO.findByStepId(ctx.pathParam("step-counter").toInt())
        if (counter != null) {
            ctx.json(counter)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }



    @OpenApi(
        summary = "Add counter",
        operationId = "addcounter",
        tags = ["StepCounter"],
        path = "/api/stepcounter",
        method = HttpMethod.POST,
        pathParams = [OpenApiParam("step-counter", Int::class, "step counter")],
        responses  = [OpenApiResponse("200")]
    )
    fun addcounter(ctx: Context) {
        val counter : Health_Coaching = jsonToObject(ctx.body())
        val coaching1 = healthDAO.save(counter)
        if (coaching1 != null) {
            counter.ID = coaching1
            ctx.json(coaching1)
            ctx.status(201)
        }
    }


    @OpenApi(
        summary = "Delete counter by ID",
        operationId = "deleteCounterById",
        tags = ["StepCounter"],
        path = "/api/stepcounter/{step-counter}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("step-counter", Int::class, "step counter")],
        responses  = [OpenApiResponse("204")]
    )
    fun deleteCounterById(ctx: Context){
        if (counterDAO.deleteByStepId(ctx.pathParam("step-counter").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }


    @OpenApi(
        summary = "Update counter by ID",
        operationId = "updateCounterById",
        tags = ["StepCounter"],
        path = "/api/stepcounter/{step-counter}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("step-counter", Int::class, "step counter")],
        responses  = [OpenApiResponse("204")]
    )
    fun updateCounterById(ctx: Context){
        val foundCounter : Step_Counter = jsonToObject(ctx.body())
        if (counterDAO.updateByStepId(stepId = ctx.pathParam("step-counter").toInt(),stepDTO = foundCounter ) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }


    // Tracking Water Intake stuff

    @OpenApi(
        summary = "Get all Water Intake",
        operationId = "getAllWaterIntake",
        tags = ["TrackingWaterIntake"],
        path = "/api/trackingwaterintake",
        method = HttpMethod.GET,
        responses = [OpenApiResponse("200", [OpenApiContent(Array<Tracking_Water_Intake>::class)])]
    )
    fun getAllWaterIntake(ctx: Context) {
        val water = waterDAO.getAll()
        if (water.size != 0) {
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(water)
    }



    @OpenApi(
        summary = "Get Water Intake by ID",
        operationId = "getWaterByID",
        tags = ["TrackingWaterIntake"],
        path = "/api/trackingwaterintake/{tracking-water-intake}",
        method = HttpMethod.GET,
        pathParams = [OpenApiParam("tracking-water-intake", Int::class, "tracking water intake")],
        responses  = [OpenApiResponse("200", [OpenApiContent(Tracking_Water_Intake::class)])]
    )
    fun getWaterByID(ctx: Context) {
        val water = waterDAO.findBywaterintakeId(ctx.pathParam("tracking-water-intake").toInt())
        if (water != null) {
            ctx.json(water)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }



    @OpenApi(
        summary = "Add water",
        operationId = "addwater",
        tags = ["TrackingWaterIntake"],
        path = "/api/trackingwaterintake",
        method = HttpMethod.POST,
        pathParams = [OpenApiParam("tracking-water-intake", Int::class, "tracking water intake")],
        responses  = [OpenApiResponse("200")]
    )
    fun addwater(ctx: Context) {
        val water : Tracking_Water_Intake = jsonToObject(ctx.body())
        val coaching1 = waterDAO.save(water)
        if (coaching1 != null) {
            water.ID = coaching1
            ctx.json(coaching1)
            ctx.status(201)
        }
    }


    @OpenApi(
        summary = "Delete water by ID",
        operationId = "deleteWaterById",
        tags = ["TrackingWaterIntake"],
        path = "/api/trackingwaterintake/{tracking-water-intake}",
        method = HttpMethod.DELETE,
        pathParams = [OpenApiParam("tracking-water-intake", Int::class, "tracking water intake")],
        responses  = [OpenApiResponse("204")]
    )
    fun deleteWaterById(ctx: Context){
        if (waterDAO.deleteBywaterintakeId(ctx.pathParam("tracking-water-intake").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }


    @OpenApi(
        summary = "Update water by ID",
        operationId = "updateWaterById",
        tags = ["TrackingWaterIntake"],
        path = "/api/trackingwaterintake/{tracking-water-intake}",
        method = HttpMethod.PATCH,
        pathParams = [OpenApiParam("tracking-water-intake", Int::class, "tracking water intake")],
        responses  = [OpenApiResponse("204")]
    )
    fun updateWaterById(ctx: Context){
        val foundWater : Tracking_Water_Intake = jsonToObject(ctx.body())
        if (waterDAO.updateBywaterintakeId(waterId = ctx.pathParam("tracking-water-intake").toInt(),waterDTO = foundWater ) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }




}
