package ie.setu.domain

import org.joda.time.DateTime

data class Exercise_goals(
    var id: Int,
    var Calories_To_Burn:Int,
    var Steps:Int,
    var Date: DateTime,
    var userId: Int
)