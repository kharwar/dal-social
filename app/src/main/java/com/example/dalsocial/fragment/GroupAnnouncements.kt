package com.example.dalsocial.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dalsocial.R

class GroupAnnouncements : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        print("Groups")
        return inflater.inflate(R.layout.fragment_event_list, container, false)
    }
}