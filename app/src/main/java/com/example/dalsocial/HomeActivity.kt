package com.example.dalsocial

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_social_page -> {
                    findNavController(R.id.activity_home_nav_host_fragment).navigate(R.id.socialFragment)
                    true
                }
                R.id.menu_events_page -> {
                    findNavController(R.id.activity_home_nav_host_fragment).navigate(R.id.eventsFragment)
                    true
                }
                R.id.menu_groups_page -> {
                    findNavController(R.id.activity_home_nav_host_fragment).navigate(R.id.eventsFragment)
                    true
                }
                R.id.menu_chat_page -> {
                    findNavController(R.id.activity_home_nav_host_fragment).navigate(R.id.chatFragment)
                    true
                }
                R.id.menu_profile_page -> {
                    findNavController(R.id.activity_home_nav_host_fragment).navigate(R.id.profileFragment)
                    true
                }
                else -> false
            }
        }


    }
}