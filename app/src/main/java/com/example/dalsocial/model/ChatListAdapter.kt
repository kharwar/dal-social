package com.example.dalsocial.model

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dalsocial.R
import com.example.dalsocial.databinding.FragmentChatCardBinding
import com.example.dalsocial.fragment.ChatFragment
import com.example.dalsocial.fragment.ChatFragmentDirections


class ChatListAdapter (var chatlist: List<ChatList>, val fragment: ChatFragment): RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        return ChatListViewHolder(
            FragmentChatCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        val chat = chatlist.get(position)
        holder.bindChatList(chat)
    }

    override fun getItemCount(): Int {
        return chatlist.size
    }


    inner class ChatListViewHolder (val chatListBinding: FragmentChatCardBinding) : RecyclerView.ViewHolder(chatListBinding.root) {
        fun bindChatList(chatlist: ChatList){
            chatListBinding.SoloChatUserName.text = chatlist.displayName

            // Setting onClick listener
            chatListBinding.SoloChatUserName.setOnClickListener { it ->
                val action = ChatFragmentDirections.actionChatFragmentToUserChatFragment(
                    userName = chatlist.displayName!!
                )
                Navigation.findNavController(it).navigate(action)

            }

            //loading image
            Log.d("ImageLoader", "Before Load")
            Glide
                .with(fragment)
                .load(chatlist.profilePic)
                .centerInside()
                .placeholder(fragment.resources.getDrawable(R.drawable.ic_baseline_replay_24))
                .into(chatListBinding.chatProfile)
            Log.d("ImageLoader", "After Load")
        }
    }
}