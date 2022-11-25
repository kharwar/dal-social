package com.example.dalsocial.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dalsocial.R
import com.example.dalsocial.databinding.FragmentChatBinding
import com.example.dalsocial.model.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var chatsList: MutableList<ChatList> = ArrayList()
        val chatListPersistence: IChatListPersistence = ChatListPersistence()
        var adapter = ChatListAdapter(chatsList, this);
        binding.chatList.layoutManager=
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.chatList.adapter = adapter

        chatListPersistence.getChatList() { chats ->
            adapter.chatlist = chats.toMutableList()
            adapter.notifyDataSetChanged()
        }

        }

}