package com.example.dalsocial.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.dalsocial.R
import com.example.dalsocial.databinding.FragmentEventBinding
import com.example.dalsocial.model.Event
import com.example.dalsocial.model.EventPersistence
import com.example.dalsocial.model.IEventPersistence
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.util.*

class EventFragment : Fragment() {

    var isRegistered = true;

    val TAG = "EventFragment"

    var binding: FragmentEventBinding? = null
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
        val eventPersistence: IEventPersistence = EventPersistence()
        var eventId: String? = arguments?.getString("eventId")
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
                                    Snackbar.make(view, message!!, Snackbar.LENGTH_SHORT).show()
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