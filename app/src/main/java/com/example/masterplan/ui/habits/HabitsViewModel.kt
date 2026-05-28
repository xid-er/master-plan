package com.example.masterplan.ui.habits

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HabitsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Your habits will be here"
    }
    val text: LiveData<String> = _text
}