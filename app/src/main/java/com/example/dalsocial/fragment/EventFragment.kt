package com.example.dalsocial.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.dalsocial.R
import com.example.dalsocial.databinding.FragmentEventBinding
import java.util.*

class EventFragment : Fragment() {
    val isRegistered = true
    var binding: FragmentEventBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEventBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toggleRegisterBtn()
        binding?.eventTitle?.text = arguments?.getString("eventTitle")
        binding?.eventDescription?.text = arguments?.getString("eventDescription")
        binding?.eventDate?.text = arguments?.getString("eventDate")
        val imageUrl = arguments?.getString("eventBg")


        Glide
            .with(this)
            .load(imageUrl)
            .centerInside()
            .placeholder(resources.getDrawable(R.drawable.ic_baseline_replay_24))
            .into(binding?.eventBg!!)
    }

    private fun toggleRegisterBtn() {
        binding?.eventRegisterBtn?.text = if(isRegistered) "Registered" else "Register"
        binding?.eventRegisterBtn?.isEnabled = !isRegistered
    }
}