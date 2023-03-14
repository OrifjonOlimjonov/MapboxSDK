package uz.orifjon.mapboxexample.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_location")
data class UserLocation(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val latitude: Double,
    val longitude: Double
)