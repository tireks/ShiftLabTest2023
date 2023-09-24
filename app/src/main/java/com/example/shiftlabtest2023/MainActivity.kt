package com.example.shiftlabtest2023

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.example.shiftlabtest2023.databinding.ActivityMainBinding
import com.example.shiftlabtest2023.screen.RegistrationFragment
import com.example.shiftlabtest2023.screen.RegistrationFragmentDirections

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val navController get() = findNavController(R.id.mainDataContainer)

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

    //TODO refactor

    //TODO add digits restriction in name

    //TODO set types for edittexts

    //TODO make fonts larger

    //TODO string resourses
}