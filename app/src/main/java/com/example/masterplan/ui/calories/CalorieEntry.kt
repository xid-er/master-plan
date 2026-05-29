package com.example.masterplan.ui.calories

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class EntryType { MEAL, EXERCISE }

@Entity(tableName = "calorie_entries")
data class CalorieEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val calories: Int,
    val type: EntryType,
    val date: String
)

@Entity(tableName = "daily_goal")
data class DailyGoal(
    @PrimaryKey val date: String,
    val goal: Int
)
