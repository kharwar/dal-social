package com.example.dalsocial.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dalsocial.R
import com.example.dalsocial.databinding.FragmentUserEmailCardBinding

class UserEmailCardFragment : Fragment() {

    lateinit var binding: FragmentUserEmailCardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserEmailCardBinding.inflate(layoutInflater)
        return binding.root
    }
}