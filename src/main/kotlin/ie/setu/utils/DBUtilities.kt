package ie.setu.utils

import ie.setu.domain.*
import ie.setu.domain.db.*
import org.jetbrains.exposed.sql.ResultRow

fun mapToUser(it: ResultRow) = User(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email]
)

fun mapToActivity(it: ResultRow) = Activity(
    id = it[Activities.id],
    description = it[Activities.description],
    duration = it[Activities.duration],
    started = it[Activities.started],
    calories = it[Activities.calories],
    userId = it[Activities.userId]
)


fun mapToHeartRate(it: ResultRow) = HeartBeat(
    id = it[HeartRate.id],
    rate = it[HeartRate.rate],
    userId = it[HeartRate.userId]
)

fun mapToStep_counter(it: ResultRow) = Step_Counter(
    ID = it[Step_counter.id],
    Daily_Steps = it[Step_counter.steps],
    UserID = it[Step_counter.userId]
)

fun mapToExercise_goal(it: ResultRow) = Exercise_goals(
    id = it[Exercise_goal.id],
    Calories_To_Burn = it[Exercise_goal.calories_to_burn],
    Steps = it[Exercise_goal.steps],
    Date = it[Exercise_goal.date],
    userId = it[Exercise_goal.userId]
)

fun mapToHealthcoaching(it: ResultRow) = Health_Coaching(
    ID = it[Health_coaching.id],
    Protein_Intake = it[Health_coaching.protein_intake],
    UserID = it[Health_coaching.userId],
    macro_percentages = it[Health_coaching.macro_percentage],
)

fun mapToTrackingwaterintake(it: ResultRow) = Tracking_Water_Intake(
    ID = it[Tracking_water_intake.id],
    Glass_of_Water = it[Tracking_water_intake.glass_of_water],
    DateTime = it[Tracking_water_intake.datetime],
    UserID = it[Tracking_water_intake.userId],
)