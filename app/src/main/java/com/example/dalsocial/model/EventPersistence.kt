package com.example.dalsocial.model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(DelicateCoroutinesApi::class)
class EventPersistence : IEventPersistence {

    val TAG = "EventPersistence"

    val db = FirebaseFirestore.getInstance()

    val EVENTS_COLLECTION: String = "events"
    val EVENTS_GUESTS_COLLECTION: String = "eventGuests"

    private val eventRef = db.collection(EVENTS_COLLECTION)
    private val guestsRef = db.collection(EVENTS_GUESTS_COLLECTION)

    private val userManagement = UserManagement()
    private val currentUser: String? = userManagement.getFirebaseUserID()

    override fun getAllEvents(result: (List<Event>) -> Unit) {

        var events: MutableList<Event> = ArrayList<Event>()

        GlobalScope.launch {
            eventRef.get().addOnSuccessListener { document ->
                events = document.toObjects(Event::class.java)
                Log.d(TAG, "Events Called")
                Log.d(TAG, "Event Size ${events.size}")
                result(events!!)
            }
                .addOnFailureListener { exception ->
                    {
                        print(exception)
                    }
                }
        }
    }

    override fun registerEvent(eventId: String, result: (Boolean) -> Unit) {
        try {
            GlobalScope.launch {
                eventRef.document(eventId).get().addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                       throw Exception("No such event exist")
                    }
                }.await()

                val ref = guestsRef.document()
                val eventGuest: EventGuest = EventGuest(
                    ref.id,
                    currentUser,
                    eventId
                )
                ref.set(eventGuest)
                result(true)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Exception in registerEvent: $e")
            result(false)
        }
    }

    override fun isCurrentUserRegistered(eventId: String, result: (Boolean) -> Unit) {
        try {
            GlobalScope.launch {
                eventRef.document(eventId).get().addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        throw Exception("No such event exist")
                    }
                }.await()
                guestsRef.whereEqualTo("userId", currentUser).get().addOnCompleteListener() { it ->
                    if(it.isSuccessful){

                    }
                }
            }
        } catch (e: Exception){
            Log.d(TAG, "Exception in isUserRegistered: $e")
            result(false)
        }
    }

}