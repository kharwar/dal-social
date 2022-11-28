package com.example.dalsocial.fragment.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dalsocial.databinding.FragmentChatBinding
import com.example.dalsocial.model.chat.ChatList
import com.example.dalsocial.model.chat.ChatListAdapter
import com.example.dalsocial.model.chat.ChatListPersistence
import com.example.dalsocial.model.chat.IChatListPersistence

class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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