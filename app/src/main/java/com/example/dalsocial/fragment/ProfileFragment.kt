package com.example.dalsocial.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.dalsocial.R
import com.example.dalsocial.model.CurrentUser
import com.example.dalsocial.model.IUserPersistence
import com.example.dalsocial.model.UserManagement
import com.example.dalsocial.model.UserPersistence
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val tvProfileDisplayName: TextView = view.findViewById(R.id.profile_name)
        val tvProfileEmail: TextView = view.findViewById(R.id.profile_email)

        val ivProfile = view.findViewById<CircleImageView>(R.id.profile_image)

        val userPersistence: IUserPersistence = UserPersistence()
        val userManagement = UserManagement()

        userManagement.getUserByID(
            userPersistence,
            userManagement.getFirebaseUserID()!!) { user ->
            Glide.with(this).load(user?.profilePictureURL).into(ivProfile)
        }

        tvProfileDisplayName.text = CurrentUser.displayName
        tvProfileEmail.text = CurrentUser.email

        val cardSocialProfile = view.findViewById<CardView>(R.id.socialProfileCard)
        cardSocialProfile.setOnClickListener {
            findNavController().navigate(R.id.socialProfileSettings)
        }

        return view;
    }
}