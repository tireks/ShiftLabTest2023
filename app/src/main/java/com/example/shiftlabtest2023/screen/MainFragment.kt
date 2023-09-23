package com.example.shiftlabtest2023.screen

import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import com.example.shiftlabtest2023.R
import com.example.shiftlabtest2023.databinding.FragmentMainBinding
import com.example.shiftlabtest2023.utils.mainActivity

class MainFragment : BaseFragment<FragmentMainBinding>(){

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater, container, false)
    }

    override fun onResume() {
        super.onResume()
        mainActivity.setSupportActionBar(binding.mainToolbar)
        setupMenu()
    }

    private fun performAccountRemoval() {
        
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