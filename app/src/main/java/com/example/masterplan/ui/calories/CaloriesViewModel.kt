package com.example.masterplan.ui.calories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.masterplan.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CaloriesViewModel(app: Application) : AndroidViewModel(app) {

    private val dao = AppDatabase.getInstance(app).calorieDao()
    private val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    val entries = dao.getEntriesForDate(today).asLiveData()

    val goalAndRemaining = dao.getEntriesForDate(today)
        .combine(dao.getGoalForDate(today)) { entries, goalRow ->
            val goal = goalRow?.goal ?: 2000
            val consumed = entries.filter { it.type == EntryType.MEAL }.sumOf { it.calories }
            val burned = entries.filter { it.type == EntryType.EXERCISE }.sumOf { it.calories }
            Pair(goal, goal - consumed + burned)
        }
        .flowOn(Dispatchers.IO)
        .asLiveData()

    fun addEntry(name: String, calories: Int, type: EntryType) = viewModelScope.launch {
        dao.insertEntry(CalorieEntry(name = name, calories = calories, type = type, date = today))
    }

    fun deleteEntry(id: Int) = viewModelScope.launch { dao.deleteEntry(id) }

    fun setGoal(goal: Int) = viewModelScope.launch { dao.upsertGoal(DailyGoal(today, goal)) }
}
