package com.example.masterplan.ui.habits

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val completedDate: String? = null  // "YYYY-MM-DD", null = never done
)
