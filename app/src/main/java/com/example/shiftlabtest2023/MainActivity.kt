package com.example.shiftlabtest2023

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.example.shiftlabtest2023.data.UserRepositoryImpl
import com.example.shiftlabtest2023.databinding.ActivityMainBinding
import com.example.shiftlabtest2023.domain.repository.UserRepository
import com.example.shiftlabtest2023.screen.RegistrationFragment
import com.example.shiftlabtest2023.screen.RegistrationFragmentDirections

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val navController get() = findNavController(R.id.mainDataContainer)
    val repository: UserRepository = UserRepositoryImpl(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    fun openAccount(nameData : String){
        val action = RegistrationFragmentDirections.actionRegistrationScreenFragmentToMainFragment(nameData)
        navController.navigate(action)
    }

    //TODO make upgrades on textfields (clear on date, visibility on password)

    //TODO spit some comments on argueable things

    //TODO refactor

}