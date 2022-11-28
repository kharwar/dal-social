package com.example.dalsocial.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.dalsocial.R
import com.example.dalsocial.R.id.btnResetPassword
import com.example.dalsocial.model.IUserManagement
import com.example.dalsocial.model.UserManagement
import com.google.android.material.textfield.TextInputEditText
import com.tapadoo.alerter.Alerter

class ResetPasswordFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reset_password, container, false)

        val userManagement: IUserManagement = UserManagement()

        val tiInputResetPassword = view.findViewById<TextInputEditText>(R.id.tiResetPasswordEmail)
        val btnResetPassword = view.findViewById<Button>(btnResetPassword)
        btnResetPassword.setOnClickListener {
            val email = tiInputResetPassword.text
            if (email != null) {
                userManagement.resetPasswordByEmail(email.toString()) { success ->
                    if (success) {
                        Alerter.create(requireActivity())
                            .setText("Email sent successfully to ${tiInputResetPassword.text}")
                            .setBackgroundColorRes(com.tapadoo.alerter.R.color.alerter_default_success_background)
                            .show()
                    } else {
                        Alerter.create(requireActivity())
                            .setText("Something went wrong")
                            .setBackgroundColorRes(R.color.md_theme_light_error)
                            .show()
                    }
                }
            } else {
                Alerter.create(requireActivity())
                    .setText("Please enter your email")
                    .setBackgroundColorRes(R.color.md_theme_light_error)
                    .show()
            }
        }

        return view
    }
}