package com.example.dalsocial.fragment.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.dalsocial.HomeActivity
import com.example.dalsocial.R
import com.example.dalsocial.activity.SetupUserActivity
import com.example.dalsocial.model.UserManagement
import com.example.dalsocial.model.UserPersistence
import com.example.dalsocial.model.states.AuthenticationSuccessState
import com.google.android.material.textfield.TextInputEditText
import com.tapadoo.alerter.Alerter

class RegistrationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = "Register";
        val view = inflater.inflate(R.layout.fragment_registration, container, false)

        val registerButton = view.findViewById<Button>(R.id.registerRegisterButton)
        val edEmail = view.findViewById<TextInputEditText>(R.id.edRegistrationEmail)
        val edPassword = view.findViewById<TextInputEditText>(R.id.edRegistrationPassword)

        registerButton.setOnClickListener {
            val email = edEmail.text.toString()
            val password = edPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {

                val auth = UserManagement()
                val persistence = UserPersistence()

                auth.registerWithEmail(email, password) { state ->
                    if (state is AuthenticationSuccessState) {

                        var intent = Intent(activity, HomeActivity::class.java)

                        persistence.getUserByID(auth.getFirebaseUserID()!!) { user ->
                            if (user == null) {
                                intent = Intent(context, SetupUserActivity::class.java)
                            }
                            startActivity(intent)
                        }

                    } else {
                        // reference for messages: https://github.com/catapulta-startup-sas/m2colab/blob/6b5deff03ff803b6d0ce08f7e4d5ee8e9f16f507/lib/firebase/handles/sign_up_handle.dart
                        // got an example message by debugging first, then looked up for that message on GitHuib and got other similar messages
                        when (state.message) {
                            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                                Alerter.create(requireActivity())
                                    .setText("The email is being used by another account.")
                                    .setBackgroundColorRes(R.color.md_theme_light_error)
                                    .show()
                            }
                            "ERROR_INVALID_EMAIL" -> {
                                Alerter.create(requireActivity())
                                    .setText("Invalid address seems incorrect")
                                    .setBackgroundColorRes(R.color.md_theme_light_error)
                                    .show()
                            }
                            "ERROR_WEAK_PASSWORD" -> {
                                Alerter.create(requireActivity())
                                    .setText("Password should be at least 6 characters")
                                    .setBackgroundColorRes(R.color.md_theme_light_error)
                                    .show()
                            }
                            else -> {
                                Alerter.create(requireActivity())
                                    .setText("An error occurred, please try again later.")
                                    .setBackgroundColorRes(R.color.md_theme_light_error)
                                    .show()
                            }
                        }
                    }
                }
            }
        }
        // Inflate the layout for this fragment
        return view
    }

}