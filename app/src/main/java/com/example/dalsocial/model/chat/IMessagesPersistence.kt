package com.example.dalsocial.model.chat

interface IMessagesPersistence {
    fun sendMessage(connectionId: String, messages: Messages, result: (Boolean) -> Unit)
//    fun getMessages(connectionId: String, result: (List<Messages>) -> Unit)
}