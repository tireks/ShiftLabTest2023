package com.example.shiftlabtest2023.domain.usecase

import com.example.shiftlabtest2023.domain.entity.User
import com.example.shiftlabtest2023.domain.repository.UserRepository

class SaveUserUseCase(
    private val repository: UserRepository
){
    suspend operator fun invoke(name : String, surname : String) =
        repository.saveUser(name, surname)
}