package com.example.shiftlabtest2023.presentation

sealed interface RegistrationState{
    object Unlocked : RegistrationState
    object Locked : RegistrationState
}