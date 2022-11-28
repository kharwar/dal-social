package com.example.dalsocial.model

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*

@OptIn(DelicateCoroutinesApi::class)
class MessagesPersistence : IMessagesPersistence {

    private val db = FirebaseFirestore.getInstance()

    private val MATCHES_COLLECTION: String = "matches"
    private val matchesRef = db.collection(MATCHES_COLLECTION)

    override fun sendMessage(connectionId: String, messages: Messages, result: (Boolean) -> Unit) {
        try {
            GlobalScope.launch {
                matchesRef.document(connectionId)
                    .collection("messages").add(messages).addOnCompleteListener{it ->
                        if(it.isSuccessful){
                            result(true)
                        } else {
                            result(false)
                        }
                    }
            }
        } catch (e: Exception) {
            print(e)
            result(false)
        }
    }
}