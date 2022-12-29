package ie.setu.domain.repository

import ie.setu.domain.Step_Counter
import ie.setu.domain.Tracking_Water_Intake
import ie.setu.domain.db.Step_counter
import ie.setu.domain.db.Tracking_water_intake
import ie.setu.utils.mapToStep_counter
import ie.setu.utils.mapToTrackingwaterintake
import org.jetbrains.exposed.sql.selectAll
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
}