package com.example.dalsocial

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val navController: NavController = Navigation.findNavController(this, R.id.activity_home_nav_host_fragment);
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation);
        Navigation.setViewNavController(bottomNavigationView, navController);
    }
}