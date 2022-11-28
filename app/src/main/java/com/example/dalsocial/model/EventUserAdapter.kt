package com.example.dalsocial.model

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dalsocial.R
import com.example.dalsocial.databinding.FragmentUserEmailCardBinding
import com.example.dalsocial.fragment.EventUserListFragment

class EventUserAdapter (var users: MutableList<User>, val fragment: EventUserListFragment): RecyclerView.Adapter<EventUserAdapter.ViewHolder>()  {

    inner class ViewHolder (val userBinding: FragmentUserEmailCardBinding): RecyclerView.ViewHolder(userBinding.root){
        fun bindEvent(user: User){
            userBinding.userEmail.text = user.email
            userBinding.userDisplayName.text = user.displayName

            userBinding.userEmailCard.setOnClickListener {
                val bundle: Bundle = bundleOf(
                    "userId" to user.userID
                )
                fragment.findNavController().navigate(R.id.action_eventUserListFragment_to_QRCodeScannedFragment2, bundle)
            }

            Glide
                .with(fragment)
                .load(user.profilePictureURL)
                .centerInside()
                .placeholder(fragment.resources.getDrawable(R.drawable.ic_baseline_replay_24))
                .into(userBinding.userProfilePicture)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(
           FragmentUserEmailCardBinding.inflate(
               LayoutInflater.from(parent.context),
               parent,
               false
           )
       )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users.get(position)
        holder.bindEvent(user)
    }

    override fun getItemCount(): Int {
        return users.size
    }
}