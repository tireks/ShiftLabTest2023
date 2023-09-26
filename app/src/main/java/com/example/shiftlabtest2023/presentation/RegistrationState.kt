package com.example.shiftlabtest2023.presentation

sealed interface RegistrationState{
    data object Unlocked : RegistrationState
    data object Locked : RegistrationState
    data object SkipScreen : RegistrationState
    data object InitializeScreen : RegistrationState
    data object InitializeContent : RegistrationState
}