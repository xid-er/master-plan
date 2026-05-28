package com.example.masterplan.ui.habits

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.masterplan.data.AppDatabase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate

class HabitsViewModel(app: Application) : AndroidViewModel(app) {

    private val dao = AppDatabase.getInstance(app).habitDao()

    @RequiresApi(Build.VERSION_CODES.O)
    val habits = dao.getAll().map { list ->
        val today = LocalDate.now().toString()
        val (done, pending) = list.partition { it.completedDate == today }
        pending + done
    }.asLiveData()

    fun addHabit(name: String) = viewModelScope.launch { dao.insert(Habit(name = name)) }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toggleHabit(habit: Habit) = viewModelScope.launch {
        val today = LocalDate.now().toString()
        dao.setCompleted(habit.id, if (habit.completedDate == today) null else today)
    }

    fun deleteHabit(id: Int) = viewModelScope.launch { dao.deleteById(id) }
}
