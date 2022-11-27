package com.example.dalsocial.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dalsocial.HomeActivity
import com.example.dalsocial.R
import com.example.dalsocial.activity.SetupUserActivity
import com.example.dalsocial.model.UserManagement
import com.example.dalsocial.model.UserPersistence
import com.example.dalsocial.model.states.AuthenticationSuccessState
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

            val email = edEmail.text
            val password = edPassword.text

            val userManagement = UserManagement()

            val persistence = UserPersistence()

            if (email == null || email.isEmpty()) {
                Alerter.create(requireActivity())
                    .setText("Email can not be empty!")
                    .setBackgroundColorRes(R.color.md_theme_light_error)
                    .show()
            } else if (password == null || password.isEmpty()) {
                Alerter.create(requireActivity())
                    .setText("You need a password to login")
                    .setBackgroundColorRes(R.color.md_theme_light_error)
                    .show()
            } else {
                userManagement.loginWithEmail(email.toString(), password.toString()) { state ->
                    //TODO: handle errors with state
                    Alerter.hide()

                    if (state is AuthenticationSuccessState) {

                        var intent = Intent(activity, HomeActivity::class.java)

                        persistence.getUserByID(userManagement.getFirebaseUserID()!!) { user ->
                            if (user == null) {
                                intent = Intent(context, SetupUserActivity::class.java)
                            }
                            startActivity(intent)
                        }

                    } else {
                        when (state.message) {
                            "ERROR_INVALID_EMAIL" -> {
                                Alerter.create(requireActivity())
                                    .setText("Invalid address seems incorrect")
                                    .setBackgroundColorRes(R.color.md_theme_light_error)
                                    .show()
                            }
                            "ERROR_USER_NOT_FOUND" -> {
                                Alerter.create(requireActivity())
                                    .setText("We couldn't find an account with that email")
                                    .setBackgroundColorRes(R.color.md_theme_light_error)
                                    .show()
                            }
                            "ERROR_WRONG_PASSWORD" -> {
                                Alerter.create(requireActivity())
                                    .setText("Uh-oh! You seem to have the entered wrong password")
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