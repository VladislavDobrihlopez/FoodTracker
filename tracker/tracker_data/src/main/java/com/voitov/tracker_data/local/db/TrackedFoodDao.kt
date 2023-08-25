package com.voitov.tracker_data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.voitov.tracker_data.local.entity.TrackedFoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackedFoodDao {
    @Query(
        """
        SELECT * FROM food_item 
        WHERE year=:year AND month=:month AND dayOfMonth=:dayOfMonth
        """
    )
    fun selectByDate(year: Int, month: Int, dayOfMonth: Int): Flow<List<TrackedFoodEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrackableFood(food: TrackedFoodEntity)

    @Delete
    fun deleteTrackedFood(food: TrackedFoodEntity)
}