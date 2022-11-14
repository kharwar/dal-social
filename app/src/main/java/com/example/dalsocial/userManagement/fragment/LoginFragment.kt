package com.example.dalsocial.userManagement.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dalsocial.HomeActivity
import com.example.dalsocial.R
import com.example.dalsocial.userManagement.activity.SetupUserActivity
import com.example.dalsocial.userManagement.model.FirebaseAuthentication
import com.example.dalsocial.userManagement.model.UserPersistence

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = "Login";

        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val loginButton = view.findViewById<Button>(R.id.loginLoginButton)
        val edEmail = view.findViewById<EditText>(R.id.edLoginEmail)
        val edPassword = view.findViewById<EditText>(R.id.edLoginPassword)

        loginButton.setOnClickListener {

            //TODO: Show progress bar here

            val email = edEmail.text.toString()
            val password = edPassword.text.toString()

            val auth = FirebaseAuthentication()

            val persistence = UserPersistence()

            auth.loginWithEmail(email, password) { success ->
                if (success) {

                    var intent = Intent(activity, HomeActivity::class.java)

                    persistence.getUserByID(auth.getFirebaseUserID()!!) { user ->
                        if (user == null) {
                            intent = Intent(context, SetupUserActivity::class.java)
                        }
                        startActivity(intent)
                    }

                } else {
                    Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val registerButton = view.findViewById<Button>(R.id.loginRegisterButton)
        registerButton.setOnClickListener {
           findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }

        return view
    }

}