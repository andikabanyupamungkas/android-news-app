package com.example.jatis.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.jatis.R
import com.example.jatis.databinding.ActivityMenuBinding
import com.example.jatis.ui.event.MainEvent
import com.example.jatis.utils.addFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuActivity : AppCompatActivity(), MainEvent {

    private val binding by lazy { ActivityMenuBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //open home fragment
        openFragment(HomeFragment())
    }

    override fun openFragment(fragment: Fragment) {
        addFragment(supportFragmentManager, R.id.root_main, fragment)
    }
}
