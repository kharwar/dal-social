package com.example.dalsocial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dalsocial.databinding.FragmentCreateEventBinding

class CreateEventFragment : Fragment() {
    var binding: FragmentCreateEventBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCreateEventBinding.inflate(layoutInflater)
        return binding?.root
    }

}