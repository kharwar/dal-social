//package com.example.dalsocial.fragment
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.dalsocial.R
//import com.example.dalsocial.databinding.FragmentEventListBinding
//import com.example.dalsocial.model.Event
//import com.example.dalsocial.model.EventAdapter
//import com.example.dalsocial.model.EventPersistence
//import com.example.dalsocial.model.IEventPersistence
//import java.util.*
//import kotlin.collections.ArrayList
//
//class EventListFragment : Fragment() {
//
//    lateinit var binding: FragmentEventListBinding
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentEventListBinding.inflate(layoutInflater)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        var eventsList: MutableList<Event> = ArrayList()
//        val eventPersistence: IEventPersistence = EventPersistence()
//
//        eventPersistence.getAllEvents() { events ->
//                val adapter = EventAdapter(events, this);
//                binding.eventList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
//                binding.eventList.adapter = adapter
//        }
//
//
//    }
//}