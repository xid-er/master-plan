package com.example.masterplan.ui.chores

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChoreDao {
    @Query("SELECT * FROM chores ORDER BY id ASC")
    fun getAll(): Flow<List<Chore>>

    @Insert
    suspend fun insert(chore: Chore)
}
