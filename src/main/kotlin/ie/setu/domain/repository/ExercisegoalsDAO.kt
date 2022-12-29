package ie.setu.domain.repository

import ie.setu.domain.Exercise_goals
import ie.setu.domain.Step_Counter
import ie.setu.domain.db.Exercise_goal
import ie.setu.domain.db.Step_counter
import ie.setu.utils.mapToExercise_goal
import ie.setu.utils.mapToStep_counter
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class ExercisegoalsDAO {

    fun getAll(): ArrayList<Exercise_goals> {
        val exercisegoalsList: ArrayList<Exercise_goals> = arrayListOf()
        transaction {
            Exercise_goal.selectAll().map {
                exercisegoalsList.add(mapToExercise_goal(it)) }
        }
        return exercisegoalsList
    }
}