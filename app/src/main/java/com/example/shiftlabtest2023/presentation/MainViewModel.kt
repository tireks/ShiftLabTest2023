package com.example.shiftlabtest2023.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel () :  ViewModel(){
    private val _state: MutableLiveData<MainState> = MutableLiveData(MainState.ShowContent)
    val state: LiveData<MainState> = _state



}