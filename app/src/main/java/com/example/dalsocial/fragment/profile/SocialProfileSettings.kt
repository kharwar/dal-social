package com.example.dalsocial.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.dalsocial.R

class SocialProfileSettings : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_social_profile_settings, container, false)

        val btnSocialProfile: Button = view.findViewById(R.id.btnSocialProfileToggle)
        val btnOtherDetails: Button = view.findViewById(R.id.btnOtherDetailsToggle)

        val fragmentContainerView: View = view.findViewById(R.id.socialProfileFragmentContainer)

        btnSocialProfile.setOnClickListener {
            fragmentContainerView.findNavController()
                .navigate(R.id.action_otherDetailsPorfileFragment_to_socialProfileFragment)
        }

        btnOtherDetails.setOnClickListener {
            fragmentContainerView.findNavController()
                .navigate(R.id.action_socialProfileFragment_to_otherDetailsPorfileFragment)
        }

        return view
    }
}