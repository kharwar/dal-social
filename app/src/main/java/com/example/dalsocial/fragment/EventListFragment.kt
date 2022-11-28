package com.example.dalsocial.fragment

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dalsocial.R
import com.example.dalsocial.databinding.FragmentEventListBinding
import com.example.dalsocial.model.Event
import com.example.dalsocial.model.EventAdapter
import com.example.dalsocial.model.EventPersistence
import com.example.dalsocial.model.IEventPersistence
import com.tapadoo.alerter.Alerter
import java.util.*
import kotlin.collections.ArrayList

class EventListFragment : Fragment() {

    private val TAG = "EventListFragment"

    private var adapter: EventAdapter? = null
    private val eventPersistence: IEventPersistence = EventPersistence()

    lateinit var binding: FragmentEventListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener("fromEventFragment") { _, bundle ->
            if(bundle.getBoolean("isEventDeleted")){
                Alerter.create(requireActivity())
                    .setText("Event Deleted")
                    .setBackgroundColorRes(com.tapadoo.alerter.R.color.alerter_default_success_background)
                    .show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var eventsList: MutableList<Event> = ArrayList()
        adapter = EventAdapter(eventsList, this);
        binding.eventList.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.eventList.adapter = adapter


//        eventPersistence.getMyEvents { events ->
//            var adapter = EventAdapter(events, this);
//            binding.eventList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
//            binding.eventList.adapter = adapter
//
//        }

        binding.eventToggle.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.allEventsBtn -> {
                        eventPersistence.getAllEvents() { events ->
                            adapter?.events = events
                            Log.d("Adapter", "all")
                            adapter?.notifyDataSetChanged()
                        }
                    }
                    R.id.myEventsBtn -> {
                        eventPersistence.getMyEvents() { events ->
                            adapter?.events = events
                            Log.d("Adapter", "my")
                            adapter?.notifyDataSetChanged()
                        }
                    }
                }
            }
        }

        binding.eventToggle.clearChecked()
        binding.eventToggle.check(R.id.allEventsBtn)
        Log.d(TAG, "Events List size: ${eventsList.size}")

        binding?.createEventBtn?.setOnClickListener { it ->
            val action = EventListFragmentDirections.actionEventsFragmentToCreateEventFragment()
            Navigation.findNavController(it).navigate(action)
        }

    }
}
