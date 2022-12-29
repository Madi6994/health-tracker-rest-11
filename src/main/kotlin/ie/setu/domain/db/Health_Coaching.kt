package ie.setu.domain.db

import ie.setu.domain.db.Exercise_goals.autoIncrement
import ie.setu.domain.db.Exercise_goals.primaryKey
import ie.setu.domain.db.Exercise_goals.references
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Health_Coaching :Table("HealthActivity"){
    val id = integer("id").autoIncrement().primaryKey()
    val protein_intake = integer("Protein_Intake")
    val macro_percentage = integer("macro_percentages")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}

