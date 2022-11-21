package com.example.dalsocial.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.dalsocial.HomeActivity
import com.example.dalsocial.R
import com.example.dalsocial.activity.SetupUserActivity
import com.example.dalsocial.model.FirebaseAuthentication
import com.example.dalsocial.model.UserPersistence

class RegistrationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = "Register";
        val view = inflater.inflate(R.layout.fragment_registration, container, false)

        val registerButton = view.findViewById<Button>(R.id.registerRegisterButton)
        val edEmail = view.findViewById<EditText>(R.id.edRegistrationEmail)
        val edPassword = view.findViewById<EditText>(R.id.edRegistrationPassword)

        registerButton.setOnClickListener {
            //TODO: Show progress bar here

            val email = edEmail.text.toString()
            val password = edPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {

                val auth = FirebaseAuthentication()
                val persistence = UserPersistence()

                auth.registerWithEmail(email, password) { success ->
                    if (success) {

                        var intent = Intent(activity, HomeActivity::class.java)

                        persistence.getUserByID(auth.getFirebaseUserID()!!) { user ->
                            if (user == null) {
                                intent = Intent(context, SetupUserActivity::class.java)
                            }
                            startActivity(intent)
                        }

                    } else {
                        Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        // Inflate the layout for this fragment
        return view
    }

}