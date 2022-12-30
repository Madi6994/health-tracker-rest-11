package ie.setu.helpers

import ie.setu.domain.*
import ie.setu.domain.db.*
import ie.setu.domain.repository.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.joda.time.DateTime

val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
val validName = "Test User 1"
val validEmail = "testuser1@test.com"

val updatedName = "Updated Name"
val updatedEmail = "Updated Email"

val users = arrayListOf<User>(
    User(name = "Alice Wonderland", email = "alice@wonderland.com", id = 1),
    User(name = "Bob Cat", email = "bob@cat.ie", id = 2),
    User(name = "Mary Contrary", email = "mary@contrary.com", id = 3),
    User(name = "Carol Singer", email = "carol@singer.com", id = 4)
)

val activities = arrayListOf<Activity>(
    Activity(id = 1, description = "Running", duration = 22.0, calories = 230, started = DateTime.now(), userId = 1),
    Activity(id = 2, description = "Hopping", duration = 10.5, calories = 80, started = DateTime.now(), userId = 1),
    Activity(id = 3, description = "Walking", duration = 12.0, calories = 120, started = DateTime.now(), userId = 2)
)


val  Calories = arrayListOf<Exercise_goals>(

    Exercise_goals(id = 1, Calories_To_Burn = 98, Steps = 55, Date = DateTime.now(), userId = 1),
    Exercise_goals(id = 2, Calories_To_Burn = 95, Steps = 53, Date = DateTime.now(), userId = 6),
    Exercise_goals(id = 5, Calories_To_Burn = 102, Steps = 60, Date = DateTime.now(), userId = 2),
    Exercise_goals(id = 4, Calories_To_Burn = 107, Steps = 63, Date = DateTime.now(), userId = 7),
    Exercise_goals(id = 3, Calories_To_Burn = 150, Steps = 85, Date = DateTime.now(), userId = 9)
)

val  Coaching = arrayListOf<Health_Coaching>(

    Health_Coaching(ID = 1, Protein_Intake = 98, macro_percentages = 55, UserID = 1),
    Health_Coaching(ID = 2, Protein_Intake = 98, macro_percentages = 55, UserID = 6),
    Health_Coaching(ID = 3, Protein_Intake = 98, macro_percentages = 55, UserID = 2),
    Health_Coaching(ID = 4, Protein_Intake = 98, macro_percentages = 55, UserID = 7),
    Health_Coaching(ID = 5, Protein_Intake = 98, macro_percentages = 55, UserID = 9)
)


val  rates = arrayListOf<HeartBeat>(
    HeartBeat(id = 1, rate = 98, userId = 1),
    HeartBeat(id = 2, rate = 100, userId = 2),
    HeartBeat(id = 3, rate = 110, userId = 3),
    HeartBeat(id = 4, rate = 130, userId = 4),
    HeartBeat(id = 5, rate = 96, userId = 5)
)



val  Counter = arrayListOf<Step_Counter>(
    Step_Counter(ID = 1, Daily_Steps = 98, UserID = 1),
    Step_Counter(ID = 1, Daily_Steps = 98, UserID = 1),
    Step_Counter(ID = 1, Daily_Steps = 98, UserID = 1),
    Step_Counter(ID = 1, Daily_Steps = 98, UserID = 1),
    Step_Counter(ID = 1, Daily_Steps = 98, UserID = 1)
)



val  Waterintake = arrayListOf<Tracking_Water_Intake>(

    Tracking_Water_Intake(ID = 1, Glass_of_Water = 4, DateTime = DateTime.now(), UserID = 1),
    Tracking_Water_Intake(ID = 2, Glass_of_Water = 6, DateTime = DateTime.now(), UserID = 2),
    Tracking_Water_Intake(ID = 3, Glass_of_Water = 3, DateTime = DateTime.now(), UserID = 5),
    Tracking_Water_Intake(ID = 4, Glass_of_Water = 4, DateTime = DateTime.now(), UserID = 7),
    Tracking_Water_Intake(ID = 5, Glass_of_Water = 5, DateTime = DateTime.now(), UserID = 9)
)





fun populateUserTable(): UserDAO {
    SchemaUtils.create(Users)
    val userDAO = UserDAO()
    userDAO.save(users.get(0))
    userDAO.save(users.get(1))
    userDAO.save(users.get(2))
    return userDAO
}
fun populateActivityTable(): ActivityDAO {
    SchemaUtils.create(Activities)
    val activityDAO = ActivityDAO()
    activityDAO.save(activities.get(0))
    activityDAO.save(activities.get(1))
    activityDAO.save(activities.get(2))
    return activityDAO
}

fun populateExercise_goalsTable(): ExercisegoalsDAO{
    SchemaUtils.create(Exercise_goal)
    val exercisegoalsDAO = ExercisegoalsDAO()
    exercisegoalsDAO.save(Calories.get(0))
    exercisegoalsDAO.save(Calories.get(1))
    exercisegoalsDAO.save(Calories.get(2))
    exercisegoalsDAO.save(Calories.get(3))
    exercisegoalsDAO.save(Calories.get(4))
    return exercisegoalsDAO
}


fun populateHealth_CoachingTable(): HealthcoachingDAO{
    SchemaUtils.create(Health_coaching)
    val healthcoachingDAO = HealthcoachingDAO()
    healthcoachingDAO.save(Coaching.get(0))
    healthcoachingDAO.save(Coaching.get(1))
    healthcoachingDAO.save(Coaching.get(2))
    healthcoachingDAO.save(Coaching.get(3))
    healthcoachingDAO.save(Coaching.get(4))
    return healthcoachingDAO
}

fun populateHeartBeatTable(): HeartBeatDAO{
    SchemaUtils.create(HeartRate)
    val heartBeatDAO = HeartBeatDAO()
    heartBeatDAO.save(rates.get(0))
    heartBeatDAO.save(rates.get(1))
    heartBeatDAO.save(rates.get(2))
    heartBeatDAO.save(rates.get(3))
    heartBeatDAO.save(rates.get(4))
    return heartBeatDAO
}


fun populateStepCounterTable(): StepcounterDAO{
    SchemaUtils.create(Step_counter)
    val stepcounterDAO = StepcounterDAO()
    stepcounterDAO.save(Counter.get(0))
    stepcounterDAO.save(Counter.get(1))
    stepcounterDAO.save(Counter.get(2))
    stepcounterDAO.save(Counter.get(3))
    stepcounterDAO.save(Counter.get(4))
    return stepcounterDAO
}


fun populateWater_IntakeTable(): TrackingwaterintakeDAO {
    SchemaUtils.create(Tracking_water_intake)
    val trackingwaterintakeDAO = TrackingwaterintakeDAO()
    trackingwaterintakeDAO.save(Waterintake.get(0))
    trackingwaterintakeDAO.save(Waterintake.get(1))
    trackingwaterintakeDAO.save(Waterintake.get(2))
    trackingwaterintakeDAO.save(Waterintake.get(3))
    trackingwaterintakeDAO.save(Waterintake.get(4))
    return trackingwaterintakeDAO
}