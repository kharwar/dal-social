package com.example.dalsocial.userManagement.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dalsocial.HomeActivity
import com.example.dalsocial.R
import com.example.dalsocial.userManagement.activity.SetupUserActivity
import com.example.dalsocial.userManagement.model.FirebaseAuthentication
import com.example.dalsocial.userManagement.model.UserPersistence

class AuthenticationLoadingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_authentication_loading, container, false)

        val auth = FirebaseAuthentication()


        if (auth.isLoggedIn()) {
            val userPersistence = UserPersistence()
            var intent = Intent(activity, HomeActivity::class.java)

            try {
                userPersistence.getUserByID(auth.getFirebaseUserID()!!) { user ->
                    if (user == null) {
                        intent = Intent(activity, SetupUserActivity::class.java)
                    }
                    startActivity(intent)
                }
            } catch (e: Exception) {
                // launch no internet connection activity
                Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show()
            }


        } else {
            findNavController().navigate(R.id.action_authenticationLoadingFragment_to_loginFragment)
        }

        return view
    }
}