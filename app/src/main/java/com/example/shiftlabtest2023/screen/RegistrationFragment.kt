package com.example.shiftlabtest2023.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.shiftlabtest2023.R
import com.example.shiftlabtest2023.databinding.FragmentRegistrationBinding
import com.example.shiftlabtest2023.presentation.RegistrationState
import com.example.shiftlabtest2023.presentation.RegistrationViewModel
import com.example.shiftlabtest2023.utils.AppTextFieldEnums
import com.example.shiftlabtest2023.utils.AppTextWatcher
import com.example.shiftlabtest2023.utils.mainActivity
import com.example.shiftlabtest2023.utils.showToast
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone


class RegistrationFragment : BaseFragment<FragmentRegistrationBinding>() {

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRegistrationBinding {
        return FragmentRegistrationBinding.inflate(inflater, container, false)
    }

    private val viewModel: RegistrationViewModel by viewModels {
        viewModelFactory {
            initializer {
                RegistrationViewModel()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::handleState)
        mainActivity.setSupportActionBar(binding.mainToolbar)
        showForm()
    }

    private fun handleState(state: RegistrationState) {
        when (state){
            is RegistrationState.Unlocked -> unlockRegistration()
            is RegistrationState.Locked -> lockRegistration()
        }
    }

    private fun showForm() {
        with (binding){
            registrationButton.setOnClickListener{handleButtonClick()}
            birthdateEditText.setOnClickListener{handleDatePick(birthdateEditText)}

            nameEditText.addTextChangedListener(AppTextWatcher {
                checkState(
                    AppTextFieldEnums.Name,
                    nameEditText.text.toString()
                )
            })
            surnameEditText.addTextChangedListener(AppTextWatcher {
                checkState(
                    AppTextFieldEnums.Surname,
                    surnameEditText.text.toString()
                )
            })
            birthdateEditText.addTextChangedListener(AppTextWatcher {
                checkState(
                    AppTextFieldEnums.Birthdate,
                    birthdateEditText.text.toString()
                )
            })
            passwordEditText.addTextChangedListener(AppTextWatcher {
                checkState(
                    AppTextFieldEnums.Password,
                    passwordEditText.text.toString()
                )
            })
            passwordConfirmEditText.addTextChangedListener(AppTextWatcher {
                checkState(
                    AppTextFieldEnums.PasswordConf,
                    passwordConfirmEditText.text.toString()
                )
            })
        }
    }

    private fun handleDatePick(birthdateEditText: TextInputEditText) {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build()
        datePicker.show(parentFragmentManager, "tag")
        datePicker.addOnPositiveButtonClickListener {
            val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            utc.timeInMillis = it
            val format = SimpleDateFormat("dd.MM.yyyy")
            birthdateEditText.setText (format.format(utc.time))
        }
    }

    private fun handleButtonClick() {
        hideAlerts()
        lifecycleScope.launch {
            val validationResult = viewModel.validateData(packData())
            handleErrors(validationResult)
        }

    }

    private fun hideAlerts() {
        binding.nameAlert.isVisible = false
        binding.passwordAlert.isVisible = false
        binding.birthdayAlert.isVisible = false
    }

    private fun handleErrors(result: MutableList<AppTextFieldEnums>) {
        var tempString = "Errors in fields:"
        if (result.isEmpty()){
            val name = binding.nameEditText.text.toString() + " " + binding.surnameEditText.text.toString()
            mainActivity.openAccount(name)
            return
        }
        for (i in result.indices){
            tempString += result[i].name
            tempString += ","
            when(result[i]){
                AppTextFieldEnums.Name ->{
                    binding.nameAlert.isVisible = true
                }
                AppTextFieldEnums.Surname -> {
                    binding.nameAlert.isVisible = true
                }
                AppTextFieldEnums.Birthdate -> {
                    binding.birthdayAlert.isVisible = true
                }
                AppTextFieldEnums.Password , AppTextFieldEnums.PasswordConf -> {
                    binding.passwordAlert.isVisible = true
                }
            }
        }
        Snackbar.make(binding.registrationButton, tempString, Snackbar.LENGTH_LONG)
            .setAnchorView(binding.bottomButtonCard)
            .setAction(getString(R.string.registration_snackbar_action)){  }
            .show()
    }

    private fun packData(): MutableList<AppTextFieldEnums> {
        val tempList = mutableListOf<AppTextFieldEnums>()
        with(binding){
            AppTextFieldEnums.Name.content = nameEditText.text.toString()
            tempList.add(AppTextFieldEnums.Name)
            AppTextFieldEnums.Surname.content = surnameEditText.text.toString()
            tempList.add(AppTextFieldEnums.Surname)
            AppTextFieldEnums.Birthdate.content = birthdateEditText.text.toString()
            tempList.add(AppTextFieldEnums.Birthdate)
            AppTextFieldEnums.Password.content = passwordEditText.text.toString()
            tempList.add(AppTextFieldEnums.Password)
            AppTextFieldEnums.PasswordConf.content = passwordConfirmEditText.text.toString()
            tempList.add(AppTextFieldEnums.PasswordConf)
        }
        return tempList
    }

    private fun checkState(enum: AppTextFieldEnums, content: String) {
        enum.content = content
        viewModel.checkState(enum)
    }

    private fun lockRegistration() {
        binding.registrationButton.isEnabled = false
        binding.registrationButtonTooltip.isVisible = true
    }

    private fun unlockRegistration() {
        binding.registrationButton.isEnabled = true
        binding.registrationButtonTooltip.isVisible = false
    }

}