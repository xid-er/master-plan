package com.example.masterplan.ui.chores

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ChoresViewModel(app: Application) : AndroidViewModel(app) {

    private val dao = ChoreDatabase.getInstance(app).choreDao()

    val chores: LiveData<List<Chore>> = dao.getAll().asLiveData()

    private val _randomChore = MutableLiveData<String?>()
    val randomChore: LiveData<String?> = _randomChore

    fun addChore(name: String) = viewModelScope.launch {
        dao.insert(Chore(name = name))
    }

    fun deleteChore(id: Int) = viewModelScope.launch {
        dao.deleteById(id)
    }

    fun pickRandom() {
        _randomChore.value = chores.value?.randomOrNull()?.name
    }
}
