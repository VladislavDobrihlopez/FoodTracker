package com.voitov.tracker_data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.voitov.tracker_data.local.entity.TrackedFoodEntity

@Database(entities = [TrackedFoodEntity::class], version = 1, exportSchema = false)
abstract class TrackedFoodDatabase : RoomDatabase() {
    companion object {
        private val MONITOR = Any()
        private const val DB_NAME = "tracked_food.db"
        private var instance: TrackedFoodDatabase? = null

        fun getInstance(context: Context): TrackedFoodDatabase {
            synchronized(MONITOR) {
                instance?.let {
                    return it
                }

                val db =
                    Room.databaseBuilder(context, TrackedFoodDatabase::class.java, DB_NAME).build()
                instance = db
                return db
            }
        }
    }
}