package ie.setu.helpers

import ie.setu.domain.Activity
import ie.setu.domain.Exercise_goals
import ie.setu.domain.HeartBeat
import ie.setu.domain.User
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Exercise_goal
import ie.setu.domain.db.Users
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.ExercisegoalsDAO
import ie.setu.domain.repository.UserDAO
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


val  rates = arrayListOf<HeartBeat>(
    HeartBeat(id = 1, rate = 98, userId = 1),
    HeartBeat(id = 2, rate = 100, userId = 2),
    HeartBeat(id = 3, rate = 96, userId = 3)
)

val  Calories = arrayListOf<Exercise_goals>(

    Exercise_goals(id = 1, Calories_To_Burn = 98, Steps = 55, Date = DateTime.now(), userId = 1),
    Exercise_goals(id = 2, Calories_To_Burn = 95, Steps = 53, Date = DateTime.now(), userId = 6),
    Exercise_goals(id = 5, Calories_To_Burn = 102, Steps = 60, Date = DateTime.now(), userId = 2),
    Exercise_goals(id = 4, Calories_To_Burn = 107, Steps = 63, Date = DateTime.now(), userId = 7),
    Exercise_goals(id = 3, Calories_To_Burn = 150, Steps = 85, Date = DateTime.now(), userId = 9)
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