package ie.setu.config

import ie.setu.controllers.HealthTrackerController
import ie.setu.controllers.HealthTrackerController.addActivity
import ie.setu.controllers.HealthTrackerController.deleteActivityByActivityId
import ie.setu.controllers.HealthTrackerController.getActivitiesByActivityId
import ie.setu.controllers.HealthTrackerController.getAllActivities
import ie.setu.controllers.HealthTrackerController.updateActivity
import ie.setu.utils.jsonObjectMapper
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.plugin.json.JavalinJackson
import io.javalin.plugin.openapi.ui.SwaggerOptions
import io.javalin.plugin.openapi.OpenApiOptions
import io.javalin.plugin.openapi.OpenApiPlugin
import io.javalin.plugin.openapi.ui.ReDocOptions
import io.javalin.plugin.rendering.vue.VueComponent
import io.swagger.v3.oas.models.info.Info
class JavalinConfig {

    fun startJavalinService(): Javalin {

        val app = Javalin.create {
            it.registerPlugin(getConfiguredOpenApiPlugin())
            it.defaultContentType = "application/json"
            //added this jsonMapper for our integration tests - serialise objects to json
            it.jsonMapper(JavalinJackson(jsonObjectMapper()))
            it.enableWebjars()
        }.apply {
            exception(Exception::class.java) { e, _ -> e.printStackTrace() }
            error(404) { ctx -> ctx.json("404 - Not Found") }
        }.start(getHerokuAssignedPort())

        registerRoutes(app)
        return app
    }

    private fun getHerokuAssignedPort(): Int {
        val herokuPort = System.getenv("PORT")
        return if (herokuPort != null) {
            Integer.parseInt(herokuPort)
        } else 7000
    }

    private fun registerRoutes(app: Javalin) {
        app.routes {
            // The @routeComponent that we added in layout.html earlier will be replaced
            // by the String inside of VueComponent. This means a call to / will load
            // the layout and display our <home-page> component.



            get("/", VueComponent("<home-page></home-page>"))
            get("/users", VueComponent("<user-overview></user-overview>"))
            get("/users/{user-id}", VueComponent("<user-profile></user-profile>"))
            get("/users/{user-id}/activities", VueComponent("<user-activity-overview></user-activity-overview>"))


            path("/api/users") {
                get(HealthTrackerController::getAllUsers)
                post(HealthTrackerController::addUser)
                path("{user-id}"){
                    get(HealthTrackerController::getUserByUserId)
                    delete(HealthTrackerController::deleteUser)
                    patch(HealthTrackerController::updateUser)
                    path("activities"){
                        get(HealthTrackerController::getActivitiesByUserId)
                        delete(HealthTrackerController::deleteActivityByUserId)
                    }
                }
                path("/email/{email}"){
                    get(HealthTrackerController::getUserByEmail)
                }
            }
            path("/api/activities") {
                get(HealthTrackerController::getAllActivities)
                post(HealthTrackerController::addActivity)
                path("{activity-id}") {
                    get(HealthTrackerController::getActivitiesByActivityId)
                    delete(HealthTrackerController::deleteActivityByActivityId)
                    patch(HealthTrackerController::updateActivity)
                }
            }
            path("/api/heartbeats") {
                get(HealthTrackerController::getAllHeartBeat)
                post(HealthTrackerController::addBeat)
                path("{heart-rate}") {
                    get(HealthTrackerController::getBeatByID)
                    delete(HealthTrackerController::deleteBeatById)
                    patch(HealthTrackerController::updateBeatById)
                }
            }

            path("/api/exercisegoals") {
                get(HealthTrackerController::getallgoals)
                post(HealthTrackerController::addGoal)
                path("{exercise-goal}") {
                    get(HealthTrackerController::getgoalByID)
                    delete(HealthTrackerController::deleteGoalById)
                    patch(HealthTrackerController::updateGoalById)
                }
            }

            path("/api/healthcoaching") {
                get(HealthTrackerController::getAllHealthCoaching)
                post(HealthTrackerController::addcoaching)
                path("{health-coaching}") {
                    get(HealthTrackerController::getCoachingByID)
                    delete(HealthTrackerController::deleteCoachingById)
                    patch(HealthTrackerController::updateCoachingById)
                }
            }

            path("/api/stepcounter") {
                get(HealthTrackerController::getAllStepCounter)
                post(HealthTrackerController::addcounter)
                path("{step-counter}") {
                    get(HealthTrackerController::getCounterByID)
                    delete(HealthTrackerController::deleteCounterById)
                    patch(HealthTrackerController::updateCounterById)
                }
            }

            path("/api/trackingwaterintake") {
                get(HealthTrackerController::getAllWaterIntake)
                post(HealthTrackerController::addwater)
                path("{tracking-water-intake}") {
                    get(HealthTrackerController::getWaterByID)
                    delete(HealthTrackerController::deleteWaterById)
                    patch(HealthTrackerController::updateWaterById)
                }
            }
//            path("/api/stepcounter") {
//                get(HealthTrackerController::)
//                post(StepcounterController::addActivity)
//                path("{activity-id}") {
//                    get(StepcounterController::getActivitiesByActivityId)
//                    delete(StepcounterController::deleteActivityByActivityId)
//                    patch(StepcounterController::updateActivity)
//                }
//            }

//            path("/api/heartbeat") {
//                get(HealthTrackerController::getAllrates)
//                post(HealthTrackerController::addHeartBeat)
//            }
        }
    }

    fun getConfiguredOpenApiPlugin() = OpenApiPlugin(
        OpenApiOptions(
            Info().apply {
                title("Health Tracker App")
                version("1.0")
                description("Health Tracker API")
            }
        ).apply {
            path("/swagger-docs") // endpoint for OpenAPI json
            swagger(SwaggerOptions("/swagger-ui")) // endpoint for swagger-ui
            reDoc(ReDocOptions("/redoc")) // endpoint for redoc
        }
    )
}