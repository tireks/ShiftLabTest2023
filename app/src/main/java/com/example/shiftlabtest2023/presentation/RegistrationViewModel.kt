package com.example.shiftlabtest2023.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shiftlabtest2023.utils.AppTextFieldEnums
import kotlinx.coroutines.launch

class RegistrationViewModel () : ViewModel(){

    private val _state: MutableLiveData<RegistrationState> = MutableLiveData(RegistrationState.Locked)
    val state: LiveData<RegistrationState> = _state
    val emptyFieldsList = mutableListOf<AppTextFieldEnums>(
        AppTextFieldEnums.Name,
        AppTextFieldEnums.Surname,
        AppTextFieldEnums.Birthdate,
        AppTextFieldEnums.Password,
        AppTextFieldEnums.PasswordConf
    )

    fun checkState(enum: AppTextFieldEnums){
        viewModelScope.launch {
            if (enum.content.isEmpty()){
                lockState()
                if (!emptyFieldsList.contains(enum)){
                    emptyFieldsList.add(enum)
                }
            } else {
                if (emptyFieldsList.contains(enum)){
                    emptyFieldsList.remove(enum)
                }
            }
            if (emptyFieldsList.isEmpty()){
                unlockState()
            }
        }
    }

    private fun unlockState() {
        _state.value = RegistrationState.Unlocked
    }

    private fun lockState() {
        _state.value = RegistrationState.Locked
    }
}