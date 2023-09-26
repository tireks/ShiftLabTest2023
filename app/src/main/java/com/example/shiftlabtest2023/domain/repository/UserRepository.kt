package com.example.shiftlabtest2023.domain.repository

import com.example.shiftlabtest2023.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getSavedUser() : Flow<User>

    suspend fun saveUser(name: String, surname: String)
}