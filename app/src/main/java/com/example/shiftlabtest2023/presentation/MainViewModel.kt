package com.example.shiftlabtest2023.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shiftlabtest2023.domain.usecase.GetSavedUserUseCase
import com.example.shiftlabtest2023.domain.usecase.SaveUserUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainViewModel (
    private val saveUserUseCase: SaveUserUseCase,
    private val getSavedUserUseCase: GetSavedUserUseCase
) :  ViewModel(){
    private val _state: MutableLiveData<MainState> = MutableLiveData(MainState.ShowContent)
    val state: LiveData<MainState> = _state


    fun deleteAccount(){
        viewModelScope.launch {
            saveUserUseCase("", "")
        }
    }

    suspend fun getName() : String{
        return viewModelScope.async {
            val tmp = getSavedUserUseCase()
            return@async tmp.name + " " + tmp.surname
        }.await()
    }

}