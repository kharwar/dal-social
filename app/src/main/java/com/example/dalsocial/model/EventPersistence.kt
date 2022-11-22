package com.example.dalsocial.model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(DelicateCoroutinesApi::class)
class EventPersistence: IEventPersistence {

    val TAG = "EventPersistence"

    val db = FirebaseFirestore.getInstance()

    override fun getAllEvents(result: (List<Event>) -> Unit){

        var events: MutableList<Event> = ArrayList<Event>()

        GlobalScope.launch {
            val eventRef = db.collection("events")

            eventRef.get().addOnSuccessListener { document ->
                events = document.toObjects(Event::class.java)
                Log.d(TAG, "Events Called")
                Log.d(TAG, "Event Size ${events.size}")
                result(events!!)
            }
                .addOnFailureListener { exception -> {
                    print(exception)
                } }
        }
    }
}