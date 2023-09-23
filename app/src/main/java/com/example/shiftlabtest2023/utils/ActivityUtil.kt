package com.example.shiftlabtest2023.utils

import androidx.fragment.app.Fragment
import com.example.shiftlabtest2023.MainActivity

val Fragment.mainActivity: MainActivity
    get() = requireActivity() as MainActivity