package com.example.dalsocial.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dalsocial.R
import com.tapadoo.alerter.Alerter

class UserSetupDisplayName : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //TODO: validation

        val view = inflater.inflate(R.layout.fragment_user_setup_display_name, container, false)

        val edDisplayName = view.findViewById<EditText>(R.id.edDisplayName)
        val btnNext = view.findViewById<View>(R.id.btnUserSetupDisplayName)

        btnNext.setOnClickListener {
            val displayName = edDisplayName.text.toString()
            val bundle = Bundle()

            val userDetailsMap = HashMap<String, Any>()
            userDetailsMap["displayName"] = displayName

            bundle.putSerializable("userDetails", userDetailsMap)

            if (displayName.isNotEmpty()) {
                findNavController().navigate(
                    R.id.action_userSetupDisplayName_to_userSetupDetails,
                    bundle
                )
            } else {
                Alerter.create(requireActivity())
                    .setText("Duh! Can't have empty display name")
                    .setBackgroundColorRes(R.color.md_theme_light_error)
                    .show()
            }

        }

        return view
    }

}