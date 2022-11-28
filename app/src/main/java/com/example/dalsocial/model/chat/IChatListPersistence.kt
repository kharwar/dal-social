package com.example.dalsocial.model.chat

interface IChatListPersistence {
    fun getChatList(result: (List<ChatList>) -> Unit)
}