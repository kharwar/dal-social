package com.example.dalsocial.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.dalsocial.MainActivity
import com.example.dalsocial.R
import com.example.dalsocial.R.*
import com.example.dalsocial.model.IUserManagement
import com.example.dalsocial.model.UserManagement

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layout.fragment_settings, container, false)

        val userManagement: IUserManagement = UserManagement()
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            userManagement.logout()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        return view
    }

}