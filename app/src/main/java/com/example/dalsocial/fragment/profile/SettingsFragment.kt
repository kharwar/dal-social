package com.example.dalsocial.fragment.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.dalsocial.MainActivity
import com.example.dalsocial.R
import com.example.dalsocial.R.layout
import com.example.dalsocial.model.IUserManagement
import com.example.dalsocial.model.IUserPersistence
import com.example.dalsocial.model.UserManagement
import com.example.dalsocial.model.UserPersistence

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layout.fragment_settings, container, false)

        val userManagement: IUserManagement = UserManagement()
        val userPersistence: IUserPersistence = UserPersistence()

        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            userManagement.logout()
            goToMainActivity()
        }

        val deleteAccount = view.findViewById<Button>(R.id.btnDeleteAccount)
        deleteAccount.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Delete your account")
                .setMessage("Are you sure you want to delete your account? All your user data will be lost. Chat and data related to events will remain.")
                .setPositiveButton("Yes") { _, _ ->
                    val userID = userManagement.getFirebaseUserID()
                    userManagement.logout()
                    goToMainActivity()
                    if (userID != null) {
                        userManagement.deleteUser(
                            userPersistence,
                            userID
                        ) {}
                    }
                }
                .setNegativeButton("No") { _, _ -> }
                .show()
        }

        return view
    }

    private fun goToMainActivity() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

}