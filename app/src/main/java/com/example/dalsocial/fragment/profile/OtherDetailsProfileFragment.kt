package com.example.dalsocial.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dalsocial.R
import com.example.dalsocial.model.user.*
import com.google.android.material.textfield.TextInputEditText
import com.tapadoo.alerter.Alerter

class OtherDetailsProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_other_details_porfile, container, false)

        val userManagement: IUserManagement = UserManagement()
        val userPersistence: IUserPersistence = UserPersistence()

        val tiInstagram = view.findViewById<TextInputEditText>(R.id.tiOtherDetailsInstagram)
        val tiTwitter = view.findViewById<TextInputEditText>(R.id.tiOtherDetailsTwitter)
        val tiFacebook = view.findViewById<TextInputEditText>(R.id.tiOtherDetailsFacebook)
        val tiLinkedIn = view.findViewById<TextInputEditText>(R.id.tiOtherDetailsLinkedIn)

        var user: User? = User()

        userManagement.getUserByID(userPersistence, userManagement.getFirebaseUserID()!!) { usr ->
            if (usr != null) {
                user = usr
                tiInstagram.setText(user!!.instagram)
                tiTwitter.setText(user!!.twitter)
                tiFacebook.setText(user!!.facebook)
                tiLinkedIn.setText(user!!.linkedin)
            }
        }

        val btnSave = view.findViewById<View>(R.id.btnOtherDetailsSave)
        btnSave.setOnClickListener {

            val instagram = tiInstagram.text
            val twitter = tiTwitter.text
            val facebook = tiFacebook.text
            val linkedin = tiLinkedIn.text

            user!!.instagram = instagram.toString()
            user!!.twitter = twitter.toString()
            user!!.facebook = facebook.toString()
            user!!.linkedin = linkedin.toString()

            userManagement.createOrUpdateUser(userPersistence, user!!) {
                if (it) {
                    Alerter.create(requireActivity())
                        .setText("Details update successfully")
                        .setBackgroundColorRes(R.color.md_theme_light_secondary)
                        .show()
                } else {
                    Alerter.create(requireActivity())
                        .setText("Error updating details")
                        .setBackgroundColorRes(R.color.md_theme_light_secondary)
                        .show()
                }
            }
        }

        return view
    }
}