package ie.setu.domain.db

import ie.setu.domain.db.Health_coaching.autoIncrement
import ie.setu.domain.db.Health_coaching.primaryKey
import ie.setu.domain.db.Health_coaching.references
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Step_counter: Table("coachingactivity") {

    val id = integer("ID").autoIncrement().primaryKey()
    val steps = integer("Daily_Steps")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}