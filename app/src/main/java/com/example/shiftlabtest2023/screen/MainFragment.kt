package com.example.shiftlabtest2023.screen

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.shiftlabtest2023.R
import com.example.shiftlabtest2023.databinding.FragmentMainBinding
import com.example.shiftlabtest2023.domain.usecase.GetSavedUserUseCase
import com.example.shiftlabtest2023.domain.usecase.SaveUserUseCase
import com.example.shiftlabtest2023.presentation.MainState
import com.example.shiftlabtest2023.presentation.MainViewModel
import com.example.shiftlabtest2023.utils.mainActivity
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainFragment : BaseFragment<FragmentMainBinding>(){

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater, container, false)
    }

    private val viewModel: MainViewModel by viewModels {
        viewModelFactory {
            initializer {
                MainViewModel(SaveUserUseCase(mainActivity.repository),
                    GetSavedUserUseCase(mainActivity.repository))
            }
        }
    }

    private val args: MainFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::handleState)
    }

    private fun handleState(state: MainState) {
        when(state){
            MainState.ShowContent -> {
                showContent()
            }
        }
    }

    private fun showContent() {
        binding.mainContentContainer.isVisible = true
        binding.accButton.setOnClickListener { lifecycleScope.launch { showPopUp() } }
        mainActivity.setSupportActionBar(binding.mainToolbar)
        setupMenu()
    }

    private suspend fun showPopUp() { //делать попап в отдельном потоке не лучшая идея,
        AlertDialog.Builder(context)  // но я не успеваю справится с тем ,что поток вывода датастора и UI поток
            .setTitle("Info")         // никак не совмещаются
            .setMessage(nameProvider())
            .show()
    }

    private suspend fun nameProvider(): String {
        return lifecycleScope.async {
            if (args.nameData.isEmpty()){
                return@async viewModel.getName()
            } else {
                return@async args.nameData
            }
        }.await()
    }

    private fun performAccountRemoval() {
        viewModel.deleteAccount()
        // в этом месте должно быть что-то для адекватного возвращения на предыдущий фрагмент,
        // обязательно синхронизовать с потоком в котором происходит запись в датастор
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.delete_menu_button -> {
                        performAccountRemoval()
                        true
                    }
                    else -> return false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}