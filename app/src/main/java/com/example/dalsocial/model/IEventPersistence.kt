package com.example.dalsocial.model

import android.net.Uri

interface IEventPersistence {
    fun getAllEvents(result: (List<Event>) -> Unit)
    fun registerEvent(eventId: String, result: (Boolean) -> Unit)
    suspend fun isCurrentUserRegistered(eventId: String?, result: (Boolean) -> Unit)
//    fun isEventExist(eventId: String, result: (Boolean) -> Unit)
    fun getMyEvents(result: (List<Event>) -> Unit)
    fun createEvent(event: Event, imageUri: Uri, result: (Event?) -> Unit)

}