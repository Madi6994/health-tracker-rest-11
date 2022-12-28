package ie.setu.domain.repository

import ie.setu.domain.HeartBeat
import ie.setu.domain.db.HeartRate
import ie.setu.utils.mapToHeartRate
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction


class HeartBeatDAO {

    /*.....*/
    fun getAll(): ArrayList<HeartBeat> {
        val rateList: ArrayList<HeartBeat> = arrayListOf()
        transaction {
            HeartRate.selectAll().map {
                rateList.add(mapToHeartRate(it)) }
        }
        return rateList
    }

    fun save(rateobj : HeartBeat){
        transaction {
            HeartRate.insert {
                it[id] = rateobj.id
                it[rate] = rateobj.rate
                it[userId] = rateobj.userId
            }
        }
    }
}