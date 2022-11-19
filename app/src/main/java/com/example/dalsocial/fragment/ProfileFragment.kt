package com.example.dalsocial.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.dalsocial.R
import com.example.dalsocial.model.CurrentUser

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val tvProfileDisplayName: TextView = view.findViewById(R.id.profile_name)
        val tvProfileEmail: TextView = view.findViewById(R.id.profile_email)

        tvProfileDisplayName.text = CurrentUser.displayName
        tvProfileEmail.text = CurrentUser.email

        val cardSocialProfile = view.findViewById<CardView>(R.id.socialProfileCard)
        cardSocialProfile.setOnClickListener {

        }

        return view;
    }
}