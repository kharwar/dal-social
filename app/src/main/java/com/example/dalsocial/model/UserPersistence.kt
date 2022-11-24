package com.example.dalsocial.model

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.example.dalsocial.cards.Cards
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(DelicateCoroutinesApi::class)
class UserPersistence : IUserPersistence {

    val db = FirebaseFirestore.getInstance()
    val storageRef = FirebaseStorage.getInstance().reference

    override fun getUserByID(id: String, result: (User?) -> Unit) {

        var user: User?

        GlobalScope.launch {
            val docRef = db.collection("users").document(id)

            docRef.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    user = document.toObject(User::class.java)
                    result(user!!)
                } else {
                    result(null)
                }

            }.addOnFailureListener { _ ->
                throw Exception("Error getting documents.")
            }.await()

        }
    }

    override fun createOrUpdateUser(user: User, result: (Boolean) -> Unit) {

        if (user.userID == null) {
            result(false)
        } else {
            GlobalScope.launch {
                db.collection("users").document(user.userID!!).set(user).addOnSuccessListener {
                    result(true)
                }.addOnFailureListener {
                    throw Exception("Error updating document.")
                }.await()
            }
        }
    }

    override fun deactivateUser(user: User, result: (Boolean) -> Unit) {
        user.isActive = false
        createOrUpdateUser(user, result)
    }

    override fun uploadImage(
        userID: String,
        imageUri: Uri,
        fileName: String,
        result: (String?) -> Unit
    ) {
        val userRef = storageRef.child("$userID/profile/$fileName")

        userRef.putFile(imageUri).addOnSuccessListener {
            userRef.downloadUrl.addOnSuccessListener {
                result(it.toString())
            }
        }.addOnFailureListener {
            result(null)
        }

//        GlobalScope.launch {
//            userRef.downloadUrl.addOnSuccessListener {
//                result(it.toString())
//            }.addOnFailureListener {
//                result(null)
//            }.await()
//        }
    }

    override fun getAllUsers(result: (ArrayList<User>) -> Unit) {

        var swipeCards: ArrayList<User> = ArrayList()
        GlobalScope.launch {
            val cardRef = db.collection("users").get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        Log.d(TAG, "${document.id} => ${document.data}")
                        swipeCards.add(document.toObject(User::class.java))
                        result(swipeCards!!)
                    }
                }
                .addOnFailureListener {
                    throw Exception("Error updating document.")
                }.await()
        }
    }
}