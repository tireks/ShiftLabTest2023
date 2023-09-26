package com.example.shiftlabtest2023.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shiftlabtest2023.domain.usecase.GetSavedUserUseCase
import com.example.shiftlabtest2023.domain.usecase.SaveUserUseCase
import com.example.shiftlabtest2023.utils.AppTextFieldEnums
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
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
            try {
                val tmp = getSavedUserUseCase()
                if (tmp.name.isEmpty() && tmp.surname.isEmpty()){
                    _state.value = RegistrationState.InitializeContent
                } else {
                    _state.value = RegistrationState.SkipScreen
                    //делать стейт для перехода на другой экран - не очень хорошо,
                    //знаю, просто слишком поздно понял, как хранение имени в датасторе реализовать в рамках
                    //clean architecture, не успел все нормально до конца сделать
                }
            } catch (e: Exception){
                Log.d("save","exception")
                //здесь должна быть нормальная работа с ошибкой,
                // дополнительный стейт вьюмодели для вывода
                // и прочее
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
        fun monthWorker(monthInt : Int) : String{
            if (monthInt < 10) {
                return "0$monthInt"
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
            todayDate.dayOfMonth.toString() + "-" + monthWorker(todayDate.month.value)  + "-" + desiredYear.toString(),
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
        return viewModelScope.async {
            val tempList = mutableListOf<AppTextFieldEnums>()
            data.forEach {
                if (!isValidData(it)) {
                    tempList.add(it)
                }
            }
            return@async tempList
        }.await()
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
        //тут надо было в трай-кэчи обернуть
    }

    private fun unlockState() {
        _state.value = RegistrationState.Unlocked
    }

    private fun lockState() {
        _state.value = RegistrationState.Locked
    }
}