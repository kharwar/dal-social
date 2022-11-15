package com.example.dalsocial.userManagement.fragment

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.dalsocial.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class UserSetupInterests : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_setup_interests, container, false)

        val edInterestsInput = view.findViewById<EditText>(R.id.edUserSetupInterests)
        val btnNext = view.findViewById<Button>(R.id.btnUserSetupInterests)
        val chipGroup = view.findViewById<ChipGroup>(R.id.chipGroupUserSetupInterests)

        // reference: https://stackoverflow.com/questions/1489852/android-handle-enter-in-an-edittext
        edInterestsInput.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                val chip = Chip(context)
                chip.text = edInterestsInput.text
                edInterestsInput.text.clear()
                chip.isCloseIconVisible = true
                chipGroup.addView(chip)
                chip.setOnCloseIconClickListener { chipGroup.removeView(chip) }
                return@OnKeyListener true
            }
            false
        })

        return view
    }

}