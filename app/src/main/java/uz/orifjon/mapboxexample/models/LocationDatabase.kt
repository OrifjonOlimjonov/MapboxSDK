package uz.orifjon.mapboxexample.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserLocation::class], version = 1, exportSchema = false)
abstract class LocationDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDao

    companion object {
        @Volatile
        private var INSTANCE: LocationDatabase? = null

        fun getDatabase(context: Context): LocationDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocationDatabase::class.java,
                    "location_database"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                return  instance
            }
        }

    }

}