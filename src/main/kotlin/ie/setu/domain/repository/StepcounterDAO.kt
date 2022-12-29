package ie.setu.domain.repository

import ie.setu.domain.Activity
import ie.setu.domain.Step_Counter
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Step_counter
import ie.setu.utils.mapToActivity
import ie.setu.utils.mapToStep_counter
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class StepcounterDAO {


    fun getAll(): ArrayList<Step_Counter> {
        val stepcounterList: ArrayList<Step_Counter> = arrayListOf()
        transaction {
            Step_counter.selectAll().map {
                stepcounterList.add(mapToStep_counter(it)) }
        }
        return stepcounterList
    }

    fun findByStepId(id: Int): Step_Counter?{
        return transaction {
            Step_counter
                .select() { Step_counter.id eq id}
                .map{ mapToStep_counter(it) }
                .firstOrNull()
        }
    }

    fun findByUserId(userId: Int): List<Step_Counter>{
        return transaction {
            Step_counter
                .select {Step_counter.userId eq userId}
                .map { mapToStep_counter(it) }
        }
    }

    fun save(stepact: Step_Counter){
        transaction {
            Step_counter.insert {
                it[id] = stepact.ID
                it[steps] = stepact.Daily_Steps
                it[userId] = stepact.UserID

            }
        }
    }


}