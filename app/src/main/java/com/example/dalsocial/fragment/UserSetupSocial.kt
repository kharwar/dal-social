package com.example.dalsocial.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.dalsocial.HomeActivity
import com.example.dalsocial.R
import com.example.dalsocial.model.FirebaseAuthentication
import com.example.dalsocial.model.IUserPersistence
import com.example.dalsocial.model.User
import com.example.dalsocial.model.UserPersistence

class UserSetupSocial : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //TODO: validation

        val view = inflater.inflate(R.layout.fragment_user_setup_social, container, false)

        val edBio: EditText = view.findViewById(R.id.edUserSetupBio)
        val edInsta: EditText = view.findViewById(R.id.edUserSetupInstagram)
        val edTwitter: EditText = view.findViewById(R.id.edUserSetupTwitter)
        val edLinkedIn: EditText = view.findViewById(R.id.edUserSetupLinkedIn)
        val edFacebook: EditText = view.findViewById(R.id.edUserSetupFacebook)

        val btnDone: Button = view.findViewById(R.id.btnUserSetupDetailsDone)

        btnDone.setOnClickListener {
            val userDetails: HashMap<String, Any> =
                arguments?.getSerializable("userDetails") as HashMap<String, Any>
            userDetails["bio"] = edBio.text.toString()
            userDetails["instagram"] = edInsta.text.toString()
            userDetails["twitter"] = edTwitter.text.toString()
            userDetails["linkedin"] = edLinkedIn.text.toString()
            userDetails["facebook"] = edFacebook.text.toString()

            userDetails["userID"] = FirebaseAuthentication().currentUser!!.uid
            userDetails["email"] = FirebaseAuthentication().currentUser!!.email!!

            val user: User = User.fromMap(userDetails)

            val userPersistence: IUserPersistence = UserPersistence()
            userPersistence.createOrUpdateUser(user) { success ->
                if (success) {
                    var intent = Intent(activity, HomeActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }
        }

        return view
    }

}