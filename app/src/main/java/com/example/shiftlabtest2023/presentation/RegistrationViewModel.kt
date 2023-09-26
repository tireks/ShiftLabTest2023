package com.example.shiftlabtest2023.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shiftlabtest2023.domain.entity.User
import com.example.shiftlabtest2023.domain.usecase.GetSavedUserUseCase
import com.example.shiftlabtest2023.domain.usecase.SaveUserUseCase
import com.example.shiftlabtest2023.utils.AppTextFieldEnums
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle

class RegistrationViewModel (
    private val getSavedUserUseCase: GetSavedUserUseCase,
    private val saveUserUseCase: SaveUserUseCase
) : ViewModel(){

    private val _state: MutableLiveData<RegistrationState> = MutableLiveData(RegistrationState.InitializeScreen)
    val state: LiveData<RegistrationState> = _state
    private val emptyFieldsList = mutableListOf<AppTextFieldEnums>(
        AppTextFieldEnums.Name,
        AppTextFieldEnums.Surname,
        AppTextFieldEnums.Birthdate,
        AppTextFieldEnums.Password,
        AppTextFieldEnums.PasswordConf
    )
    private var password = ""
    private var passwordConf = ""

    fun askForSavedUser(){
        viewModelScope.launch{
            /*try {
                val tmp = getSavedUserUseCase()
                Log.d("save",tmp.name)
                if (tmp.name.isEmpty() && tmp.surname.isEmpty()){
                    _state.value = RegistrationState.InitializeContent
                } else {
                    _state.value = RegistrationState.SkipScreen
                }
            } catch (e: Exception){
                Log.d("save","exception")
            }*/
            val tmp = getSavedUserUseCase()
            Log.d("save",tmp.name)
            if (tmp.name.isEmpty() && tmp.surname.isEmpty()){
                _state.value = RegistrationState.InitializeContent
            } else {
                _state.value = RegistrationState.SkipScreen
            }
        }
    }

    private fun isValidData(enum: AppTextFieldEnums): Boolean {
        var passwordString = ""
        when(enum){
            AppTextFieldEnums.Name,  AppTextFieldEnums.Surname-> {
                var tempString = enum.content
                if (enum.content.length <= 2 || enum.content.contains(Regex("[0-9]"))){
                    return false
                }
                return true
            }
            AppTextFieldEnums.Birthdate -> {
                return isBirthdateValid(enum.content)
            }
            AppTextFieldEnums.Password -> {
                password = enum.content
                return isPasswordsEquals()
            }
            AppTextFieldEnums.PasswordConf ->{
                passwordConf = enum.content
                return isPasswordsEquals()
            }
        }
    }

    private fun isPasswordsEquals(): Boolean {
        if (password.isNotEmpty() && passwordConf.isNotEmpty() &&  password != passwordConf){
            return false
        }
        return true
    }

    private fun isBirthdateValid(stringDate: String) : Boolean{
        fun monther(monthInt : Int) : String{
            if (monthInt < 10) {
                return "0" + monthInt.toString()
            }
            return monthInt.toString()
        }
        val inputDate = LocalDate.parse(
            stringDate.replace(".", "-"),
            DateTimeFormatter
                .ofPattern("dd-MM-uuuu")
                .withResolverStyle(ResolverStyle.STRICT)
        )
        val todayDate = LocalDate.now()
        val desiredYear = todayDate.year - 18
        val desiredDate = LocalDate.parse(
            todayDate.dayOfMonth.toString() + "-" + monther(todayDate.month.value)  + "-" + desiredYear.toString(),
            DateTimeFormatter
                .ofPattern("dd-MM-uuuu")
                .withResolverStyle(ResolverStyle.STRICT)
        )
        if (inputDate < desiredDate){
            return true
        }
        return false
    }

    suspend fun validateData(data : MutableList<AppTextFieldEnums>) : MutableList<AppTextFieldEnums>{
        password = ""
        passwordConf = ""
        val returnalData = viewModelScope.async {
            val tempList = mutableListOf<AppTextFieldEnums>()
            data.forEach {
                if (!isValidData(it)) {
                    tempList.add(it)
                }
            }
            return@async tempList
        }

        return returnalData.await()
    }

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

    fun getButton(){
        _state.value = RegistrationState.Unlocked
    }

    fun saveName(name : String, surname : String){
        viewModelScope.launch {
            saveUserUseCase(name, surname)
        }
    }

    private fun unlockState() {
        _state.value = RegistrationState.Unlocked
    }

    private fun lockState() {
        _state.value = RegistrationState.Locked
    }
}