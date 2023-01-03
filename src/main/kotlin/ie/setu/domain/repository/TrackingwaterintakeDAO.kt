package ie.setu.domain.repository

import ie.setu.domain.Step_Counter
import ie.setu.domain.Tracking_Water_Intake
import ie.setu.domain.db.Step_counter
import ie.setu.domain.db.Tracking_water_intake
import ie.setu.utils.mapToStep_counter
import ie.setu.utils.mapToTrackingwaterintake
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class TrackingwaterintakeDAO {

    fun getAll(): ArrayList<Tracking_Water_Intake> {
        val trackingwaterintakeList: ArrayList<Tracking_Water_Intake> = arrayListOf()
        transaction {
            Tracking_water_intake.selectAll().map {
                trackingwaterintakeList.add(mapToTrackingwaterintake(it)) }
        }
        return trackingwaterintakeList
    }

    fun findBywaterintakeId(id: Int): Tracking_Water_Intake?{
        return transaction {
            Tracking_water_intake
                .select() { Tracking_water_intake.id eq id}
                .map{ mapToTrackingwaterintake(it) }
                .firstOrNull()
        }
    }

    fun findByUserId(userId: Int): List<Tracking_Water_Intake>{
        return transaction {
            Tracking_water_intake
                .select {Tracking_water_intake.userId eq userId}
                .map { mapToTrackingwaterintake(it) }
        }
    }

    fun save(wateract: Tracking_Water_Intake) : Int?{
        return transaction {
            Tracking_water_intake.insert {
                it[id] = wateract.ID
                it[glass_of_water] = wateract.Glass_of_Water
                it[datetime] = wateract.DateTime
                it[userId] = wateract.UserID
            } get Tracking_water_intake.id
        }
    }

    fun updateBywaterintakeId(waterId: Int, waterDTO: Tracking_Water_Intake) : Int{
        return transaction {
            Tracking_water_intake.update ({
                Tracking_water_intake.id eq waterId}) {
                it[glass_of_water] = waterDTO.Glass_of_Water
                it[datetime] = waterDTO.DateTime
                it[userId] = waterDTO.UserID
            }
        }
    }

    fun deleteBywaterintakeId (waterId: Int): Int{
        return transaction{
            Tracking_water_intake.deleteWhere { Tracking_water_intake.id eq waterId }
        }
    }

    fun deleteByUserId (userId: Int): Int{
        return transaction{
            Tracking_water_intake.deleteWhere { Tracking_water_intake.userId eq userId }
        }
    }
}