package com.example.dalsocial.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import com.example.dalsocial.R
import android.view.ViewGroup
import com.example.dalsocial.databinding.FragmentEventCardBinding

class EventCardFragment : Fragment() {
    lateinit var binding: FragmentEventCardBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventCardBinding.inflate(layoutInflater)
        return binding.root
    }
}