package com.example.dalsocial.model

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

@OptIn(DelicateCoroutinesApi::class)
class MessagesPersistence : IMessagesPersistence {

    private val db = FirebaseFirestore.getInstance()

    private val MATCHES_COLLECTION: String = "matches"
    private val USERS_COLLECTION: String = "users"
    private val matchesRef = db.collection(MATCHES_COLLECTION)
    private val usersRef = db.collection(USERS_COLLECTION)

    val userManagement = UserManagement()
    private val currentUser: String? = userManagement.getFirebaseUserID()

    override fun sendMessage(connectionId: String, messages: Messages, result: (Boolean) -> Unit) {
        try {
            GlobalScope.launch {
                Log.d("res", "here "+connectionId)
                matchesRef.document(connectionId)
                    .collection("messages").add(messages).addOnCompleteListener{it ->
                        if(it.isSuccessful){
                            Log.d("respond","yes")
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
//    override fun getMessages(connectionId: String, result: (List<Messages>) -> Unit) {
//        try {
//            var messages: MutableList<Messages> = ArrayList<Messages>()
//            GlobalScope.launch {
//                Log.d("res", "here "+connectionId)
//                matchesRef.document(connectionId)
//                    .collection("messages")
//                    .get()
//                    .addOnSuccessListener { document ->
//                        messages = document.toObjects(Messages::class.java)
//                        result(messages!!)
//                    }
//                    .addOnFailureListener { exception ->
//                        run {
//                            print(exception)
//                        }
//                    }
//            }
//        } catch (e: Exception) {
//            print(e)
//        }
//    }
}