package com.example.dalsocial.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dalsocial.HomeActivity
import com.example.dalsocial.R
import com.example.dalsocial.activity.SetupUserActivity
import com.example.dalsocial.model.UserManagement
import com.example.dalsocial.model.UserPersistence
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.tapadoo.alerter.Alerter

class LoginFragment : Fragment() {

    lateinit var googleSignInClient: GoogleSignInClient

    private val userManagement = UserManagement()
    private lateinit var loginWithGoogleForResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)

        // https://stackoverflow.com/a/69404960/8348987
        loginWithGoogleForResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                userManagement.loginWithGoogle(result.data!!) { success ->
                    if (success) {
                        Snackbar.make(requireView(), "Signed in with Google", Snackbar.LENGTH_LONG)
                            .show()

                    } else {
                        Snackbar.make(
                            requireView(),
                            "Something went wrong",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = "Login";

        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val loginButton = view.findViewById<Button>(R.id.loginLoginButton)
        val edEmail = view.findViewById<TextInputEditText>(R.id.edLoginEmail)
        val edPassword = view.findViewById<TextInputEditText>(R.id.edLoginPassword)

        loginButton.setOnClickListener {

            Alerter.create(requireActivity())
                .setText("Signing you in...")
                .setBackgroundColorRes(R.color.md_theme_light_secondary)
                .show()

            val email = edEmail.text.toString()
            val password = edPassword.text.toString()

            val userManagement = UserManagement()

            val persistence = UserPersistence()

            userManagement.loginWithEmail(email, password) { success ->
            //TODO: handle errors with state
                Alerter.hide()

                if (success) {

                    var intent = Intent(activity, HomeActivity::class.java)

                    persistence.getUserByID(userManagement.getFirebaseUserID()!!) { user ->
                        if (user == null) {
                            intent = Intent(context, SetupUserActivity::class.java)
                        }
                        startActivity(intent)
                    }

                } else {
                    Alerter.create(requireActivity())
                        .setText("Sorry! Something went wrong")
                        .setBackgroundColorRes(R.color.md_theme_light_secondary)
                        .show()
                }
            }
        }

        val registerButton = view.findViewById<Button>(R.id.loginRegisterButton)
        registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }

        val loginWithGoogle = view.findViewById<Button>(R.id.loginWithGoogleButton)
        loginWithGoogle.setOnClickListener {
            val intent = googleSignInClient.signInIntent
            loginWithGoogleForResult.launch(intent)
        }

        return view
    }

}