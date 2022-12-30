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
            HeartRate.selectAll().map {
                heartbeatList.add(mapToHeartRate(it)) }
        }
        return heartbeatList
    }

    fun findByheartId(id: Int): HeartBeat?{
        return transaction {
            HeartRate
                .select() { HeartRate.id eq id}
                .map{ mapToHeartRate(it) }
                .firstOrNull()
        }
    }


    fun findByUserId(userId: Int): List<HeartBeat>{
        return transaction {
            HeartRate
                .select {HeartRate.userId eq userId}
                .map { mapToHeartRate(it) }
        }
    }

    fun save(stepact: HeartBeat) : Int?{
        return transaction {
            HeartRate.insert {

                it[rate] = stepact.rate
                it[userId] = stepact.userId

            } get HeartRate.id
        }
    }

    fun updateByheartId(heartId: Int, heartDTO: HeartBeat) : Int {
        return transaction {
            HeartRate.update ({
                HeartRate.id eq heartId}) {
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