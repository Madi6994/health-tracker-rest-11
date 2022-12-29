package ie.setu.domain.repository

import ie.setu.domain.Activity
import ie.setu.domain.Step_Counter
import ie.setu.domain.db.Activities
import ie.setu.utils.mapToActivity
import ie.setu.utils.mapToStep_counter
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class StepcounterDAO {


    fun getAll(): ArrayList<Step_Counter> {
        val stepcounterList: ArrayList<Step_Counter> = arrayListOf()
        transaction {
            Activities.selectAll().map {
                stepcounterList.add(mapToStep_counter(it)) }
        }
        return stepcounterList
    }
}