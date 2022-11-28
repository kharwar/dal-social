package com.example.dalsocial.model.events

import android.net.Uri
import com.example.dalsocial.model.user.User

interface IEventPersistence {
    fun getAllEvents(result: (List<Event>) -> Unit)
    fun registerEvent(eventId: String, result: (Boolean) -> Unit)
    suspend fun isCurrentUserRegistered(eventId: String?, result: (Boolean) -> Unit)
    fun isCurrentUserOwner(eventId: String?, result: (Boolean) -> Unit)
//    fun isEventExist(eventId: String, result: (Boolean) -> Unit)
    fun getMyEvents(result: (List<Event>) -> Unit)
    fun createEvent(event: Event, imageUri: Uri, result: (Event?) -> Unit)
    fun deleteEvent(eventId: String?, result: (Boolean) -> Unit)
    fun viewMembers(eventId: String?, result: (MutableList<User>) -> Unit)
}