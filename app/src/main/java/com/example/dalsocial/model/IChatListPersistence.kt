package com.example.dalsocial.model

interface IChatListPersistence {
    fun getChatList(result: (List<ChatList>) -> Unit)
}