package ie.setu.helpers

import ie.setu.domain.*
import ie.setu.domain.db.*
import ie.setu.domain.repository.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.joda.time.DateTime


//User
val nonExistingEmail = "112233445566778testUser@xxxxx.xx"

val validName = "Test User 1"
val validEmail = "testuser1@test.com"

val updatedName = "Updated Name"
val updatedEmail = "Updated Email"

//Activity
val nonExistingdiscription = "Running"

val activityid = 1
val activitydiscription="running"
val activityduration= 20.0
val activitycalories = 110
val activitydatetime = DateTime.now()
val activityuserid= 4

//update
val updatedactivityid = 1
val updatedactivitydiscription="running"
val updatedactivityduration= 20.0
val updatedactivitycalories = 110
val updatedactivitydatetime = DateTime.now()
val updatedactivityuserid= 4



//Exercise Goals
val nonExistingid = 6

val exerciseid = 1
val exercisecaloriestoburn= 120
val exercisesteps= 20
val exercisedatetime = DateTime.now()
val exerciseuserid= 4

//update
val updatedexerciseid = 1
val updatedexercisecaloriestoburn=111
val updatedexercisesteps= 20
val updatedexercisedatetime = DateTime.now()
val updatedexerciseuserid= 4


//Health_Coaching
val coachingid=1
val coachingproteinintake = 43
val coachingmacropercentage= 75
val coachinguserid = 3

//update
val updatedcoachingid=1
val updatedcoachingproteinintake = 63
val updatedcoachingmacropercentage= 45
val updatedcoachinguserid = 5



//HeartBeat
val nonExistingrate = 96

val heartbeatid= 0
val heartbeatrate= 98
val heartbeatuserid = 4

//update
val updatedheartbeatid= 0
val updatedheartbeatrate= 102
val updatedheartbeatuserid = 2


//Step_Counter
val nonExistingdailysteps = 45

val stepcounterid = 3
val stepcounterdailysteps= 95
val stepcounteruserid = 5

//update
val updatestepcounterid = 3
val updatestepcounterdailysteps= 95
val updatestepcounteruserid = 5


//Tracking_Water_Intake
val nonExistingglassofwater = 7

val waterintakeid = 1
val waterintakeglassofwater = 5
val waterintakedatetime = DateTime.now()
val waterintakeuserid = 4

//update
val updatewaterintakeid = 1
val updatewaterintakeglassofwater = 5
val updatewaterintakedatetime = DateTime.now()
val updatewaterintakeuserid = 4





val users = arrayListOf<User>(
    User(name = "Alice Wonderland", email = "alice@wonderland.com", id = 1),
    User(name = "Bob Cat", email = "bob@cat.ie", id = 2),
    User(name = "Mary Contrary", email = "mary@contrary.com", id = 3),
    User(name = "Carol Singer", email = "carol@singer.com", id = 4),
    User(name = "Carl Sier", email = "carl@sier.com", id = 5)
)

val activities = arrayListOf<Activity>(
    Activity(id = 1, description = "Running", duration = 22.0, calories = 230, started = DateTime.now(), userId = 1),
    Activity(id = 2, description = "Hopping", duration = 10.5, calories = 80, started = DateTime.now(), userId = 1),
    Activity(id = 3, description = "Walking", duration = 12.0, calories = 120, started = DateTime.now(), userId = 2),
    Activity(id = 4, description = "Walking", duration = 18.0, calories = 190, started = DateTime.now(), userId = 4)
)


val  Calories = arrayListOf<Exercise_goals>(

    Exercise_goals(id = 1, Calories_To_Burn = 98, Steps = 55, Date = DateTime.now(), userId = 1),
    Exercise_goals(id = 2, Calories_To_Burn = 95, Steps = 53, Date = DateTime.now(), userId = 2),
    Exercise_goals(id = 3, Calories_To_Burn = 102, Steps = 60, Date = DateTime.now(), userId = 3),
    Exercise_goals(id = 4, Calories_To_Burn = 107, Steps = 63, Date = DateTime.now(), userId = 4),
    Exercise_goals(id = 5, Calories_To_Burn = 150, Steps = 85, Date = DateTime.now(), userId = 5)
)

val  Coaching = arrayListOf<Health_Coaching>(

    Health_Coaching(ID = 1, Protein_Intake = 98, macro_percentages = 55, UserID = 1),
    Health_Coaching(ID = 2, Protein_Intake = 99, macro_percentages = 56, UserID = 2),
    Health_Coaching(ID = 3, Protein_Intake = 100, macro_percentages = 57, UserID = 3),
    Health_Coaching(ID = 4, Protein_Intake = 101, macro_percentages = 58, UserID = 4),
    Health_Coaching(ID = 5, Protein_Intake = 102, macro_percentages = 59, UserID = 5)
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
    Step_Counter(ID = 2, Daily_Steps = 109, UserID = 2),
    Step_Counter(ID = 3, Daily_Steps = 78, UserID = 3),
    Step_Counter(ID = 4, Daily_Steps = 34, UserID = 4),
    Step_Counter(ID = 5, Daily_Steps = 98, UserID = 5)
)



val  Waterintake = arrayListOf<Tracking_Water_Intake>(

    Tracking_Water_Intake(ID = 1, Glass_of_Water = 4, DateTime = DateTime.now(), UserID = 1),
    Tracking_Water_Intake(ID = 2, Glass_of_Water = 6, DateTime = DateTime.now(), UserID = 2),
    Tracking_Water_Intake(ID = 3, Glass_of_Water = 3, DateTime = DateTime.now(), UserID = 3),
    Tracking_Water_Intake(ID = 4, Glass_of_Water = 4, DateTime = DateTime.now(), UserID = 4),
    Tracking_Water_Intake(ID = 5, Glass_of_Water = 5, DateTime = DateTime.now(), UserID = 5)
)





fun populateUserTable(): UserDAO {
    SchemaUtils.create(Users)
    val userDAO = UserDAO()
    userDAO.save(users.get(0))
    userDAO.save(users.get(1))
    userDAO.save(users.get(2))
    userDAO.save(users.get(3))
    userDAO.save(users.get(4))
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