package com.example.dalsocial.fragment.profile

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
import com.example.dalsocial.model.user.IUserPersistence
import com.example.dalsocial.model.user.UserManagement
import com.example.dalsocial.model.user.UserPersistence
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

            tvProfileDisplayName.text = user?.displayName
            tvProfileEmail.text = userManagement.getFirebaseUserEmail()

            if (user?.profilePictureURL != null && user.profilePictureURL != "") {
                Glide.with(this).load(user.profilePictureURL).into(ivProfile)
            } else {
                Glide.with(this).load(R.drawable.ic_baseline_account_circle_24).into(ivProfile)
            }
        }



        val cardSocialProfile = view.findViewById<CardView>(R.id.socialProfileCard)
        cardSocialProfile.setOnClickListener {
            findNavController().navigate(R.id.socialProfileSettings)
        }

        val cardQrCode = view.findViewById<CardView>(R.id.qrCodeCard)
        cardQrCode.setOnClickListener {
            findNavController().navigate(R.id.QRCodeLanding)
        }

        val cardSettings = view.findViewById<CardView>(R.id.settingsCard)
        cardSettings.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }

        val accountsCard = view.findViewById<CardView>(R.id.accountDetailsCard)
        accountsCard.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_accountDetailsFragment)
        }

        return view;
    }
}