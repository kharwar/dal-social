package com.example.dalsocial.model

interface IEventPersistence {
    fun getAllEvents(result: (List<Event>) -> Unit)
//    fun getMyEvents(userId: String, result: (List<Event>) -> Unit)
//    fun createEvent(event: Event, result: (Boolean) -> Unit)
}