package ie.setu.utils

import ie.setu.domain.Activity
import ie.setu.domain.HeartBeat
import ie.setu.domain.Step_Counter
import ie.setu.domain.User
import ie.setu.domain.db.Activities
import ie.setu.domain.db.HeartRate
import ie.setu.domain.db.Step_counter
import ie.setu.domain.db.Users
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


