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
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

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
            userManagement.resetPasswordByEmail(tiInputResetPassword.text.toString()) { success ->
                if (success) {
                    Snackbar.make(view, "Email sent", Snackbar.LENGTH_LONG).show()
                } else {
                    Snackbar.make(view, "Something went wrong", Snackbar.LENGTH_LONG).show()
                }
            }
        }

        return view
    }
}