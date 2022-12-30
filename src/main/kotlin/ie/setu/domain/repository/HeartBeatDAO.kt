package ie.setu.domain.repository

import ie.setu.domain.HeartBeat
import ie.setu.domain.Step_Counter
import ie.setu.domain.db.HeartRate
import ie.setu.domain.db.Step_counter
import ie.setu.utils.mapToHeartRate
import ie.setu.utils.mapToStep_counter
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction


class HeartBeatDAO {

    /*.....*/
    fun getAll(): ArrayList<HeartBeat> {
        val heartbeatList: ArrayList<HeartBeat> = arrayListOf()
        transaction {
            Step_counter.selectAll().map {
                heartbeatList.add(mapToHeartRate(it)) }
        }
        return heartbeatList
    }

//    fun save(rateobj : HeartBeat){
//        transaction {
//            HeartRate.insert {
//                it[id] = rateobj.id
//                it[rate] = rateobj.rate
//                it[userId] = rateobj.userId
//            }
//        }
//    }
}