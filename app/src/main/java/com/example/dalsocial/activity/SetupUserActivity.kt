package com.example.dalsocial.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dalsocial.R

class SetupUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_user)
        title = "Hello"
    }
}