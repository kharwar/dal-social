package com.example.dalsocial.model

interface IEventPersistence {
    fun getAllEvents(result: (List<Event>) -> Unit)
    fun registerEvent(eventId: String, result: (Boolean) -> Unit)
    fun isCurrentUserRegistered(eventId: String, result: (Boolean) -> Unit)
//    fun isEventExist(eventId: String, result: (Boolean) -> Unit)
//    fun getMyEvents(userId: String, result: (List<Event>) -> Unit)
//    fun createEvent(event: Event, result: (Boolean) -> Unit)

}