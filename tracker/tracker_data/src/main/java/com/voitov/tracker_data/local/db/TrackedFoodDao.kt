package com.voitov.tracker_data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.voitov.tracker_data.local.entity.TrackableFoodEntity
import com.voitov.tracker_data.local.entity.TrackedFoodEntity
import com.voitov.tracker_domain.model.CustomTrackableFood
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
    fun insertTrackedFood(food: TrackedFoodEntity)

    @Delete
    fun deleteTrackedFood(food: TrackedFoodEntity)

    @Delete
    fun deleteCustomTrackableFood(food: TrackableFoodEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCustomTrackableFood(food: TrackableFoodEntity)

    @Query("SELECT * FROM trackable_food")
    fun getAllCustomTrackableFood(): Flow<List<TrackableFoodEntity>>
}