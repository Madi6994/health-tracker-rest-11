package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object HeartRate : Table("HeartActivity") {
    val id = integer("id").autoIncrement().primaryKey()
    val rate = integer("rate")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}


