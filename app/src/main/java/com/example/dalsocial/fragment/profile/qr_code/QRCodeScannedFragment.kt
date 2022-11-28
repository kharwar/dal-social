package com.example.dalsocial.fragment.profile.qr_code

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.dalsocial.R
import com.example.dalsocial.model.*

class QRCodeScannedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_q_r_code_scanned, container, false)

        val scannedUserId = arguments?.getString("userId")

        val userManagement: IUserManagement = UserManagement()
        val userPersistence: IUserPersistence = UserPersistence()

        val ivProfilePic = view.findViewById<ImageView>(R.id.ivScannedUserImage)
        val tvName = view.findViewById<TextView>(R.id.tvScannedName)
        val tvDisplayName = view.findViewById<TextView>(R.id.tvScannedDisplayName)
        val tvBio = view.findViewById<TextView>(R.id.tvScannedBio)

        val facebookButton = view.findViewById<ImageView>(R.id.btnScannedUserFacebook)
        val linkedinButton = view.findViewById<ImageView>(R.id.btnScannedUserLinkedIn)
        val instagramButton = view.findViewById<ImageView>(R.id.btnScannedUserInstagram)
        val twitterButton = view.findViewById<ImageView>(R.id.btnScannedUserTwitter)

        userManagement.getUserByID(userPersistence, scannedUserId!!) { user ->
            if (user != null) {
                if (user.profilePictureURL != null && user.profilePictureURL != "") {
                    Glide.with(this).load(user.profilePictureURL).into(ivProfilePic)
                } else {
                    ivProfilePic.visibility = View.GONE
                }

                tvName.text = "${user.firstName} ${user.lastName}"
                tvDisplayName.text = "(${user.displayName})"
                tvBio.text = user.bio

                if (user.facebook != null) {
                    facebookButton.visibility = View.VISIBLE
                    facebookButton.setOnClickListener {
                        val url = "https://www.facebook.com/${user.facebook}"
                        launchURL(url)
                    }
                }

                if (user.linkedin != null) {
                    linkedinButton.visibility = View.VISIBLE
                    linkedinButton.setOnClickListener {
                        val url = "https://www.linkedin.com/in/${user.linkedin}"
                        launchURL(url)
                    }
                }

                if (user.instagram != null) {
                    instagramButton.visibility = View.VISIBLE
                    instagramButton.setOnClickListener {
                        val url = "https://www.instagram.com/${user.instagram}"
                        launchURL(url)
                    }
                }

                if (user.twitter != null) {
                    twitterButton.visibility = View.VISIBLE
                    twitterButton.setOnClickListener {
                        val url = "https://www.twitter.com/${user.twitter}"
                        launchURL(url)
                    }
                }

            }
        }

        val matchPersistence: IMatchPersistence = MatchPersistence()
        val socialMatches = SocialMatches(matchPersistence, userManagement)

        val matchButton = view.findViewById<TextView>(R.id.btnScannedUserMessage)

        val includedUsers =
            mutableListOf(scannedUserId, userManagement.getFirebaseUserID()!!)
        socialMatches.hasAnyMatchByIncludedUsersID(includedUsers) { hasMatch ->
            if (hasMatch) {
                matchButton.isEnabled = false
            }
        }

        matchButton.setOnClickListener {
            val match = Match(
                approved = true,
                matchInitiatorUserId = userManagement.getFirebaseUserID()!!,
                toBeMatchedUserId = scannedUserId,
                matchInitiatorUserIdLiked = true,
                includedUsers = arrayListOf(userManagement.getFirebaseUserID()!!, scannedUserId)
            )
            socialMatches.createMatch(match) { success ->
                if (success) {
                    matchButton.text = "Matched!"
                    matchButton.isEnabled = false
                }
            }
        }

        return view
    }

    // reference to open URL: https://stackoverflow.com/questions/2201917/how-can-i-open-a-url-in-androids-web-browser-from-my-application
    fun launchURL(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

}