package com.example.dalsocial.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dalsocial.R
import com.example.dalsocial.databinding.FragmentEventUserListBinding
import com.example.dalsocial.databinding.FragmentUserEmailCardBinding
import com.example.dalsocial.model.EventPersistence
import com.example.dalsocial.model.EventUserAdapter
import com.example.dalsocial.model.IEventPersistence
import com.example.dalsocial.model.User

class EventUserListFragment : Fragment() {

    lateinit var binding: FragmentEventUserListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEventUserListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        var users: MutableList<User> = ArrayList()

        val eventPersistence: IEventPersistence = EventPersistence()



        eventPersistence.viewMembers(arguments?.getString("eventId")) { users ->
            val adapter = EventUserAdapter(users, this)
            binding.eventUserList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            binding.eventUserList.adapter = adapter
        }
    }
}