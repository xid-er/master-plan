package com.example.masterplan.ui.chores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChoresViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is chores Fragment"
    }
    val text: LiveData<String> = _text
}