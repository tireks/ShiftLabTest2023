package com.example.shiftlabtest2023.presentation

sealed interface MainState{
    data object ShowContent : MainState
    //знаю что толку мало от одного стейта, но это вроде как заглушка,
    // фрагмент то по структуре простой
}