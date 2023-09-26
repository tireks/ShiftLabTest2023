package com.example.shiftlabtest2023.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.shiftlabtest2023.domain.entity.User
import com.example.shiftlabtest2023.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


const val nameDataStore = "USER"

val Context.datastore : DataStore<Preferences> by preferencesDataStore(name = nameDataStore)

class UserRepositoryImpl(private val context: Context) : UserRepository {

    companion object{
        val nameUser = stringPreferencesKey("NAME")
        val surnameUser = stringPreferencesKey("SURNAME")

    }

    override suspend fun getSavedUser() = context.datastore.data.map { user ->
        User(
            name = user[nameUser]!!,
            surname = user[surnameUser]!!
        )
    }

    override suspend fun saveUser(name: String, surname: String) {
        context.datastore.edit { user->
            user[nameUser] = name
            user[surnameUser]= surname
        }
    }

}