package ie.setu.domain.repository

import ie.setu.domain.HeartBeat
import ie.setu.domain.Step_Counter
import ie.setu.domain.db.HeartRate
import ie.setu.domain.db.Step_counter
import ie.setu.utils.mapToHeartRate
import ie.setu.utils.mapToStep_counter
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
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

    fun findByheartId(id: Int): HeartBeat?{
        return transaction {
            HeartRate
                .select() { Step_counter.id eq id}
                .map{ mapToHeartRate(it) }
                .firstOrNull()
        }
    }


    fun findByUserId(userId: Int): List<HeartBeat>{
        return transaction {
            HeartRate
                .select {Step_counter.userId eq userId}
                .map { mapToHeartRate(it) }
        }
    }

    fun save(stepact: HeartBeat){
        transaction {
            HeartRate.insert {
                it[id] = stepact.id
                it[rate] = stepact.rate
                it[userId] = stepact.userId

            }
        }
    }

    fun updateByheartId(heartId: Int, heartDTO: HeartBeat){
        transaction {
            HeartRate.update ({
                Step_counter.id eq heartId}) {
                it[rate] = heartDTO.rate
                it[userId] = heartDTO.userId
            }
        }
    }

    fun deleteByheartId (heartId: Int): Int{
        return transaction{
            HeartRate.deleteWhere { HeartRate.id eq heartId }
        }
    }


    fun deleteByUserId (userId: Int): Int{
        return transaction{
            HeartRate.deleteWhere { HeartRate.userId eq userId }
        }
    }

}