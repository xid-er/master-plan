package com.example.masterplan.ui.calories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CaloriesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Your calories will be here"
    }
    val text: LiveData<String> = _text
}