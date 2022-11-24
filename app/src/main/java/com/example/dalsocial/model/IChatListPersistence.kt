package com.example.dalsocial.model

interface IChatListPersistence {
    fun getChatList(result: (List<ChatList>) -> Unit)
    fun getGroupChatList(result: (List<ChatList>) -> Unit)
}