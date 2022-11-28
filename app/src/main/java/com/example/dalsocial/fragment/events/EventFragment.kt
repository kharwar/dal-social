package com.example.dalsocial.fragment.events

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.dalsocial.R
import com.example.dalsocial.databinding.FragmentEventBinding
import com.example.dalsocial.model.events.EventPersistence
import com.example.dalsocial.model.events.IEventPersistence
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import com.tapadoo.alerter.Alerter;

class EventFragment : Fragment() {

    var isRegistered = true;

    val TAG = "EventFragment"

    var binding: FragmentEventBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.action_eventFragment_to_eventsFragment)
        }
        callback.isEnabled = true
    }

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
        setFragmentResultListener("requestKey") {_, bundle ->
            if(bundle.getBoolean("isAdded")){
//                Snackbar.make(view, "Event created", Snackbar.LENGTH_SHORT).show()
                Alerter.create(requireActivity())
                    .setText("Hooray! Your event is available for others now.")
                    .setBackgroundColorRes(com.tapadoo.alerter.R.color.alerter_default_success_background)
                    .show()
            }
        }
        val eventPersistence: IEventPersistence = EventPersistence()
        var eventId: String? = arguments?.getString("eventId")


        eventPersistence.isCurrentUserOwner(eventId) { isOwner ->
            if(isOwner){
                binding?.eventDeleteFb?.visibility = View.VISIBLE
                binding?.eventViewMembers?.visibility = View.VISIBLE
            }
        }


        // On View Members Listener
        binding?.eventViewMembers?.setOnClickListener {
            val action = EventFragmentDirections.actionEventFragmentToEventUserListFragment(
                eventId = eventId!!
            )
            Navigation.findNavController(it).navigate(action)
        }

        // On Delete Listener
        val alertBuilder = AlertDialog.Builder(context)
        binding?.eventDeleteFb?.setOnClickListener {
            alertBuilder.setMessage("Are you sure you want to delete the event?")
                .setCancelable(true)

            alertBuilder.setPositiveButton("Yes") { dialog, which ->
                eventPersistence.deleteEvent(eventId) {
                    val bundle: Bundle = bundleOf(
                        "isEventDeleted" to true
                    )
                    setFragmentResult("fromEventFragment", bundle)
                }
                findNavController().navigate(R.id.action_eventFragment_to_eventsFragment)
            }

            alertBuilder.setNegativeButton("No") { dialog, which ->
                dialog.cancel()
            }

            alertBuilder.show()
        }

        GlobalScope.launch {

            eventPersistence.isCurrentUserRegistered(eventId?.trim()) { registered ->
                isRegistered = registered
                toggleRegisterBtn()
                Log.d(TAG, isRegistered.toString())
                binding?.eventTitle?.text = arguments?.getString("eventTitle")
                binding?.eventDescription?.text = arguments?.getString("eventDescription")
                binding?.eventDate?.text = arguments?.getString("eventDate")
                val imageUrl = arguments?.getString("eventBg")
                Glide
                    .with(view)
                    .load(imageUrl)
                    .centerInside()
                    .placeholder(resources.getDrawable(R.drawable.ic_baseline_replay_24))
                    .into(binding?.eventBg!!)
                Log.d(TAG, "Loaded")

                binding?.eventRegisterBtn?.setOnClickListener {
                    if (eventId != null) {
                        var message: String? = "Test";
                        val responseMsg = GlobalScope.launch {
                            eventPersistence.registerEvent(eventId) { success ->
                                if (success) {
                                    message = "You have been registered for the event!"
                                    binding?.eventRegisterBtn?.isEnabled = false
                                    binding?.eventRegisterBtn?.text = "Registered"
                                    Alerter.create(requireActivity())
                                        .setText("Event looks exciting! You have been registered for it.")
                                        .setBackgroundColorRes(com.tapadoo.alerter.R.color.alerter_default_success_background)
                                        .show()

                                } else {
                                    message = "Registration failed!"
                                    Snackbar.make(view, message!!, Snackbar.LENGTH_SHORT).show()
                                }
                            }
                        }
                        runBlocking {
                            responseMsg.join()
                        }

                    }
                }
            }


        }


    }



    private fun toggleRegisterBtn() {
        binding?.eventRegisterBtn?.text = if (isRegistered) "Registered" else "Register"
        binding?.eventRegisterBtn?.isEnabled = !isRegistered
    }
}