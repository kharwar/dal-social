package com.example.dalsocial.fragment.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dalsocial.R
import com.example.dalsocial.databinding.FragmentEventCardBinding
import com.example.dalsocial.databinding.FragmentGroupCardBinding

class GroupCardFragment : Fragment() {
    lateinit var binding: FragmentGroupCardBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGroupCardBinding.inflate(layoutInflater)
        return binding.root
    }
}