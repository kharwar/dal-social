package com.example.dalsocial.model.events

import android.net.Uri
import android.util.Log
import com.example.dalsocial.model.user.User
import com.example.dalsocial.model.user.UserManagement
import com.example.dalsocial.model.user.UserPersistence
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

@OptIn(DelicateCoroutinesApi::class)
class EventPersistence : IEventPersistence {

    val TAG = "EventPersistence"

    val db = FirebaseFirestore.getInstance()

    val EVENTS_COLLECTION: String = "events"
    val EVENTS_GUESTS_COLLECTION: String = "eventGuests"
    val USERS_COLLECTION: String = "users"

    private val eventRef = db.collection(EVENTS_COLLECTION)
    private val guestsRef = db.collection(EVENTS_GUESTS_COLLECTION)
    private val userRef = db.collection(USERS_COLLECTION)
    private val storageRef = FirebaseStorage.getInstance().reference

    val userManagement = UserManagement()
    val userPersistence = UserPersistence()

    override fun getAllEvents(result: (List<Event>) -> Unit) {

        var events: MutableList<Event> = ArrayList<Event>()

        GlobalScope.launch {
            eventRef.get().addOnSuccessListener { document ->
                events = document.toObjects(Event::class.java)
                result(events)
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
                                if (documents.isEmpty) {
                                    result(false)
                                } else {
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

    override fun isCurrentUserOwner(eventId: String?, result: (Boolean) -> Unit) {
        if (eventId != null && eventId.isNotEmpty()) {
            getCurrentUser { user ->
                eventRef.document(eventId).get()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val event = it.result
                            if (event.getString("userId") == user?.userID) {
                                result(true)
                            } else {
                                result(false)
                            }
                        } else {
                            result(false)
                        }
                    }
            }
        }
    }

    override fun deleteEvent(eventId: String?, result: (Boolean) -> Unit) {
        if (eventId != null && !eventId.isEmpty()) {
            eventRef.document(eventId).delete().addOnSuccessListener {
                guestsRef.whereEqualTo("eventId", eventId).get().addOnSuccessListener { snapshot ->
                    snapshot.forEach { doc ->
                        doc.reference.delete()
                        result(true)
                    }
                }
            }
                .addOnFailureListener {
                    result(false)
                }

        }
    }

    override fun viewMembers(eventId: String?, result: (MutableList<User>) -> Unit) {
        getCurrentUser { user ->
            var guestUserIdList: MutableList<String?> = mutableListOf()
            guestsRef.whereEqualTo("eventId", eventId).get()
                .addOnSuccessListener { snapshot ->
//                    guestUserList = snapshot.toObjects(EventGuest::class.java)
//                    if(guestUserList.isNotEmpty()){
//                        var iterator = guestUserList.iterator()
//                        iterator.forEach {
//                            if(it.userId != user?.userID){
//                                guestUserList.add(it)
//                            }
//                        }
//                        userRef.whereIn("userID", )
//                    }
                    snapshot.forEach {
                        if (it.getString("userId") != user?.userID) {
                            guestUserIdList.add(it.getString("userId"))
                        }
                    }

                    userRef.whereIn("userID", guestUserIdList).get()
                        .addOnSuccessListener { userSnapshot ->
                            var users: MutableList<User> = mutableListOf()
                            users = userSnapshot.toObjects(User::class.java)
                            result(users)
                        }

                }

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
                        if (it.isSuccessful && !it.result.isEmpty) {
                            val eventIds =
                                it.result.documents.map { doc -> doc.getString("eventId") }
                            eventRef.whereIn("eventId", eventIds)
                                .get()
                                .addOnCompleteListener { documents ->
                                    events = documents.result.toObjects(Event::class.java)
                                    result(events)
                                }
                        } else {
                            result(events)
                        }
                    }
            }
        }
    }

    override fun createEvent(event: Event, imageUri: Uri, result: (Event?) -> Unit) {
        if (imageUri == null) {
            result(null)
        }
        getCurrentUser { user ->
            try {
                val ref = eventRef.document()
                event.userId = user?.userID
                event.eventId = ref.id

                uploadImage(ref.id, imageUri) {
                    event.imageUrl = it
                    ref.set(event)

                    registerEvent(event.eventId!!) { success ->
                        if (success) {
                            result(event)
                        } else {
                            result(null)
                        }
                    }


                }

            } catch (e: Exception) {
                Log.d(TAG, "Exception in createEvent: $e")
                result(null)
            }
        }
    }

    private fun uploadImage(eventId: String, imageUri: Uri, result: (String?) -> Unit): String? {
        var url: String? = null
        val fileName = getFileExtension(imageUri)
        val eventRef = storageRef.child("events/$eventId/$fileName")
        eventRef.putFile(imageUri).addOnSuccessListener {
            eventRef.downloadUrl.addOnSuccessListener {
                result(it.toString())
            }
        }.addOnFailureListener {
            Log.d(TAG, "Error in uploading Image")
            result(null)
        }
        return url
    }

    private fun getFileExtension(uri: Uri): String? {
        return uri.toString().substring(uri.toString().lastIndexOf("/") + 1);
    }

    private fun getCurrentUser(result: (User?) -> Unit) {
        userManagement.getUserByID(userPersistence, userManagement.getFirebaseUserID()!!) { user ->
            result(user)
        }
    }
}