package com.example.shiftlabtest2023.domain.usecase

import com.example.shiftlabtest2023.domain.entity.User
import com.example.shiftlabtest2023.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetSavedUserUseCase (
    private val repository: UserRepository
){
    suspend operator fun invoke() : User =
        repository.getSavedUser()
}