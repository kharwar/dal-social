package com.example.dalsocial.userManagement.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.example.dalsocial.R

class UserSetupDisplayName : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_setup_display_name, container, false)

        val edDisplayName = view.findViewById<EditText>(R.id.edDisplayName)
        val btnNext = view.findViewById<View>(R.id.btnUserSetupDisplayName)

        btnNext.setOnClickListener {
            val displayName = edDisplayName.text.toString()
            val bundle = Bundle()
            bundle.putString("displayName", displayName)

            findNavController().navigate(R.id.action_userSetupDisplayName_to_userSetupDetails, bundle)
        }

        return view
    }

}