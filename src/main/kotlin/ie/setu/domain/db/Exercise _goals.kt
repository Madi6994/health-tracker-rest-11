package ie.setu.domain.db

import ie.setu.domain.db.HeartRate.autoIncrement
import ie.setu.domain.db.HeartRate.integer
import ie.setu.domain.db.HeartRate.primaryKey
import ie.setu.domain.db.HeartRate.references
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Exercise_goals : Table("ExerciseActivity") {
    val id = integer("id").autoIncrement().primaryKey()
    val calories_to_burn = integer("Calories_to_Burn")
    val steps = integer("Steps")
    val date = integer("Date")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}



