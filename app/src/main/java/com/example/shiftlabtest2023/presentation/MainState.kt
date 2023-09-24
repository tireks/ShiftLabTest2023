package com.example.shiftlabtest2023.presentation

sealed interface MainState{
    data object ShowContent : MainState
}