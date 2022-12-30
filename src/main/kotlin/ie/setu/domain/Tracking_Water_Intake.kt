package ie.setu.domain

import org.joda.time.DateTime

data class Tracking_Water_Intake (
    var ID:Int,
    var Glass_of_Water:Int,
    var DateTime: DateTime,
    var UserID:Int
        )
