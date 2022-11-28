package com.example.dalsocial.model.events

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dalsocial.R
import com.example.dalsocial.databinding.FragmentEventCardBinding
import com.example.dalsocial.fragment.events.EventListFragment
import com.example.dalsocial.fragment.events.EventListFragmentDirections


    class EventAdapter (var events: List<Event>, val fragment: EventListFragment): RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            FragmentEventCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events.get(position)
        holder.bindEvent(event)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    inner class EventViewHolder (val eventsBinding: FragmentEventCardBinding) : RecyclerView.ViewHolder(eventsBinding.root) {
        fun bindEvent(event: Event){
            eventsBinding.eventTitle.text = event.title
            eventsBinding.eventDescription.text = event.description
            eventsBinding.eventDate.text = event.scheduledDate

            // Setting onClick listener
            eventsBinding.eventCard.setOnClickListener { it ->
                val action = EventListFragmentDirections.actionEventsFragmentToEventFragment(
                    eventBg = event.imageUrl!!,
                    eventDate = event.scheduledDate!!,
                    eventTitle =  event.title!!,
                    eventDescription = event.description!!,
                    eventId = event.eventId!!
                )
                Navigation.findNavController(it).navigate(action)

            }

            //loading image
            Log.d("ImageLoader", "Before Load")
            Glide
                .with(fragment)
                .load(event.imageUrl)
                .centerInside()
                .placeholder(fragment.resources.getDrawable(R.drawable.ic_baseline_replay_24))
                .into(eventsBinding.eventBg)
            Log.d("ImageLoader", "After Load")
        }
    }
}