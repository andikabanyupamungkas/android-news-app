package com.example.jatis.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun addFragment(fragmentManager: FragmentManager, containerId: Int, fragment: Fragment) {
    fragmentManager.beginTransaction()
        .add(containerId, fragment, fragment.tag)
        .addToBackStack(fragment.tag)
        .commit()
}