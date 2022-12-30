package ie.setu.domain.db

import ie.setu.domain.db.Health_coaching.autoIncrement
import ie.setu.domain.db.Health_coaching.primaryKey
import ie.setu.domain.db.Health_coaching.references
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Tracking_water_intake : Table("WaterIntakeActivity") {
    val id = integer("ID").autoIncrement().primaryKey()
    val glass_of_water = integer("Glass_of_Water")
    val datetime = datetime("DateTime")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}












