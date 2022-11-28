package com.example.dalsocial.fragment.setup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.dalsocial.activity.HomeActivity
import com.example.dalsocial.R
import com.example.dalsocial.model.user.UserManagement
import com.example.dalsocial.model.user.IUserPersistence
import com.example.dalsocial.model.user.User
import com.example.dalsocial.model.user.UserPersistence
import com.google.android.material.textfield.TextInputEditText

class UserSetupSocial : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_setup_social, container, false)

        val edBio: TextInputEditText = view.findViewById(R.id.edUserSetupBio)
        val edInsta: TextInputEditText = view.findViewById(R.id.edUserSetupInstagram)
        val edTwitter: TextInputEditText = view.findViewById(R.id.edUserSetupTwitter)
        val edLinkedIn: TextInputEditText = view.findViewById(R.id.edUserSetupLinkedIn)
        val edFacebook: TextInputEditText = view.findViewById(R.id.edUserSetupFacebook)

        val btnDone: Button = view.findViewById(R.id.btnUserSetupDetailsDone)

        btnDone.setOnClickListener {
            val userDetails: HashMap<String, Any> =
                arguments?.getSerializable("userDetails") as HashMap<String, Any>
            userDetails["bio"] = edBio.text.toString()
            userDetails["instagram"] = edInsta.text.toString()
            userDetails["twitter"] = edTwitter.text.toString()
            userDetails["linkedin"] = edLinkedIn.text.toString()
            userDetails["facebook"] = edFacebook.text.toString()

            userDetails["userID"] = UserManagement().currentUser!!.uid
            userDetails["email"] = UserManagement().currentUser!!.email!!

            val user: User = User.fromMap(userDetails)

            val userPersistence: IUserPersistence = UserPersistence()
            userPersistence.createOrUpdateUser(user) { success ->
                if (success) {
                    val intent = Intent(activity, HomeActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }
        }

        return view
    }

}