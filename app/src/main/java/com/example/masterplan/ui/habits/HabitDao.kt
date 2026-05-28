package com.example.masterplan.ui.habits

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits ORDER BY id ASC")
    fun getAll(): Flow<List<Habit>>

    @Insert
    suspend fun insert(habit: Habit)

    @Query("UPDATE habits SET completedDate = :date WHERE id = :id")
    suspend fun setCompleted(id: Int, date: String?)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteById(id: Int)
}
