package com.example.dalsocial.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.dalsocial.databinding.FragmentUsersChatBinding

class UserChatFragment : Fragment() {
    var binding: FragmentUsersChatBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsersChatBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.userName?.text = arguments?.getString("userName")
        binding?.backChatBtn?.setOnClickListener{ it ->
            Navigation.findNavController(it).navigate(UserChatFragmentDirections.actionUsersChatFragmentToChatFragment())
        }
    }
}