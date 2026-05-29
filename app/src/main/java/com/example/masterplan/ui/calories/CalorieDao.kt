package com.example.masterplan.ui.calories

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CalorieDao {
    @Query("SELECT * FROM calorie_entries WHERE date = :date ORDER BY id ASC")
    fun getEntriesForDate(date: String): Flow<List<CalorieEntry>>

    @Insert
    suspend fun insertEntry(entry: CalorieEntry)

    @Query("DELETE FROM calorie_entries WHERE id = :id")
    suspend fun deleteEntry(id: Int)

    @Query("SELECT * FROM daily_goal WHERE date = :date LIMIT 1")
    fun getGoalForDate(date: String): Flow<DailyGoal?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertGoal(goal: DailyGoal)
}
