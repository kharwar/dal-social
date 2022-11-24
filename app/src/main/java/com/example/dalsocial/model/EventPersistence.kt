package com.example.dalsocial.model

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

@OptIn(DelicateCoroutinesApi::class)
class EventPersistence : IEventPersistence {

    val TAG = "EventPersistence"

    val db = FirebaseFirestore.getInstance()

    val EVENTS_COLLECTION: String = "events"
    val EVENTS_GUESTS_COLLECTION: String = "eventGuests"

    private val eventRef = db.collection(EVENTS_COLLECTION)
    private val guestsRef = db.collection(EVENTS_GUESTS_COLLECTION)

    val userManagement = UserManagement()
    val userPersistence = UserPersistence()

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
                getCurrentUser { currentUser ->

                    val ref = guestsRef.document()
                    val eventGuest: EventGuest = EventGuest(
                        ref.id,
                        currentUser?.userID,
                        eventId.trim()
                    )
                    ref.set(eventGuest)
                    result(true)
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "Exception in registerEvent: $e")
            result(false)
        }
    }

    override suspend fun isCurrentUserRegistered(eventId: String?, result: (Boolean) -> Unit) {
        try {
            if (eventId == null) {
                result(false)
            } else {
                    userManagement.getUserByID(
                        userPersistence,
                        userManagement.getFirebaseUserID()!!
                    ) { user ->
                        guestsRef
                            .whereEqualTo("userId", user?.userID.toString())
                            .whereEqualTo("eventId", eventId.toString())
                            .limit(1)
                            .get()
                            .addOnCompleteListener { it ->
                                if (it.isSuccessful) {
                                    val documents = it.result
                                    Log.d(
                                        TAG,
                                        "Documents Received: " + documents.documents.size.toString()
                                    )
                                    if (documents.isEmpty) {
                                        result(false)
                                    } else {
                                        Log.d(
                                            TAG,
                                            "Received userId: ${documents.documents[0].getString("userId")}"
                                        )
                                        Log.d(TAG, "Current userId: ${user?.userID.toString()}")
                                        Log.d(
                                            TAG,
                                            "User Match: ${documents.documents[0].getString("userId") == user?.userID}"
                                        )
                                        result(true)
                                    }
                                } else {
                                    result(false)
                                }
                            }
                    }

                }
            } catch (e: Exception) {
            Log.d(TAG, "Exception in isUserRegistered: $e")
            result(false)
        }
    }

    override fun getMyEvents(result: (List<Event>) -> Unit) {
        GlobalScope.launch {
            getCurrentUser { user ->
                var events: MutableList<Event> = ArrayList<Event>()
                guestsRef
                    .whereEqualTo("userId", user?.userID)
                    .get()
                    .addOnCompleteListener {
                        it
                        val eventIds = it.result.documents.map { doc -> doc.getString("eventId") }
                        eventRef.whereIn("eventId", eventIds)
                            .get()
                            .addOnCompleteListener { documents ->
                                events = documents.result.toObjects(Event::class.java)
                                result(events)
                            }
                    }
            }
        }
    }

    override fun createEvent(event: Event, result: (Boolean) -> Unit) {
        TODO("Not yet implemented")
        // TODO: Add the organizer in eventGuests
    }

    private fun getCurrentUser(result: (User?) -> Unit) {
        userManagement.getUserByID(userPersistence, userManagement.getFirebaseUserID()!!) { user ->
            result(user)
        }
    }
}