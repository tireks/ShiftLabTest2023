package com.example.shiftlabtest2023.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.shiftlabtest2023.databinding.FragmentRegistrationBinding
import com.example.shiftlabtest2023.presentation.RegistrationState
import com.example.shiftlabtest2023.presentation.RegistrationViewModel
import com.example.shiftlabtest2023.utils.AppTextFieldEnums
import com.example.shiftlabtest2023.utils.AppTextWatcher


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