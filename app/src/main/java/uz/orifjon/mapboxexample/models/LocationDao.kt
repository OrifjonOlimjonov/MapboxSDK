package uz.orifjon.mapboxexample.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDao {


    @Insert
    fun addLocation(location: UserLocation)


    @Query("SELECT * FROM user_location")
    fun getLocations(): List<UserLocation>


    @Query("SELECT * FROM user_location ORDER BY ID DESC LIMIT 1")
    fun getLastLocation(): UserLocation
}