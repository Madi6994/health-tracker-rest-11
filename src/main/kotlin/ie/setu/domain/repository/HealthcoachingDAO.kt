package ie.setu.domain.repository

import ie.setu.domain.Health_Coaching
//import ie.setu.domain.Step_Counter
//import ie.setu.domain.Tracking_Water_Intake
import ie.setu.domain.db.Health_coaching
import ie.setu.utils.mapToHealthcoaching
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class HealthcoachingDAO {

    fun getAll(): ArrayList<Health_Coaching> {
        val healthcoachingList: ArrayList<Health_Coaching> = arrayListOf()
        transaction {
            Health_coaching.selectAll().map {
                healthcoachingList.add(mapToHealthcoaching(it)) }
        }
        return healthcoachingList
    }

    fun findBycoachingId(id: Int): Health_Coaching?{
        return transaction {
            Health_coaching
                .select() { Health_coaching.id eq id}
                .map{ mapToHealthcoaching(it) }
                .firstOrNull()
        }
    }

    fun findByUserId(userId: Int): List<Health_Coaching>{
        return transaction {
            Health_coaching
                .select {Health_coaching.userId eq userId}
                .map { mapToHealthcoaching(it) }
        }
    }

    fun save(coachact: Health_Coaching) : Int?{
        return transaction {
            Health_coaching.insert {
                it[id] = coachact.ID
                it[protein_intake] = coachact.Protein_Intake
                it[macro_percentage] = coachact.macro_percentages
                it[userId] = coachact.UserID
            } get Health_coaching.id
        }
    }

    fun updateBycoachId(coachId: Int, coachDTO: Health_Coaching) : Int{
        return transaction {
            Health_coaching.update ({
                Health_coaching.id eq coachId}) {
                it[protein_intake] = coachDTO.Protein_Intake
                it[macro_percentage] = coachDTO.macro_percentages
                it[userId] = coachDTO.UserID
            }
        }
    }

    fun deleteBycoachId (coachact: Int): Int{
        return transaction{
            Health_coaching.deleteWhere { Health_coaching.id eq coachact }
        }
    }

    fun deleteByUserId (userId: Int): Int{
        return transaction{
            Health_coaching.deleteWhere { Health_coaching.userId eq userId }
        }
    }
}