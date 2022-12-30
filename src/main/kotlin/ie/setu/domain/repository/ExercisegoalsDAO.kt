package ie.setu.domain.repository

import ie.setu.domain.Exercise_goals
import ie.setu.domain.Step_Counter
import ie.setu.domain.db.Exercise_goal
import ie.setu.domain.db.Step_counter
import ie.setu.utils.mapToExercise_goal
import ie.setu.utils.mapToStep_counter
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
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

    fun findByexerciseId(id: Int): Exercise_goals?{
        return transaction {
            Exercise_goal
                .select() { Exercise_goal.id eq id}
                .map{ mapToExercise_goal(it) }
                .firstOrNull()
        }
    }

    fun findByUserId(userId: Int): List<Exercise_goals>{
        return transaction {
            Exercise_goal
                .select {Exercise_goal.userId eq userId}
                .map { mapToExercise_goal(it) }
        }
    }

    fun save(exercisegoal: Exercise_goals){
        transaction {
            Exercise_goal.insert {
//                it[id] = exercisegoal.id
                it[calories_to_burn] = exercisegoal.Calories_To_Burn
                it[steps] = exercisegoal.Steps
                it[date] = exercisegoal.Date
                it[userId] = exercisegoal.userId
            } get Exercise_goal.id
        }
    }

    fun updateByexerciseId(exercId: Int, exercDTO: Exercise_goals){
        transaction {
            Exercise_goal.update ({
                Exercise_goal.id eq exercId}) {
                it[calories_to_burn] = exercDTO.Calories_To_Burn
                it[steps] = exercDTO.Steps
                it[date] = exercDTO.Date
                it[userId] = exercDTO.userId
            }
        }
    }

    fun deleteByexerciseId (exercId: Int): Int{
        return transaction{
            Exercise_goal.deleteWhere { Exercise_goal.id eq exercId }
        }
    }

    fun deleteByUserId (userId: Int): Int{
        return transaction{
            Exercise_goal.deleteWhere { Exercise_goal.userId eq userId }
        }
    }
}