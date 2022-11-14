package com.example.dalsocial.userManagement.model

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(DelicateCoroutinesApi::class)
class UserPersistence : IUserPersistence {

    val db = FirebaseFirestore.getInstance()

    override fun getUserByID(id: String, result: (User?) -> Unit) {

        var user: User? = User()

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
                result(null)
            }.await()

        }
    }

    override fun createOrUpdateUser(user: User, result: (Boolean) -> Unit) {

        if (user.userID == null) {
            result(false)
        } else {
            GlobalScope.launch {
                db.collection("users").document(user.userID).set(user).addOnSuccessListener {
                    result(true)
                }.addOnFailureListener {
                    result(false)
                }.await()
            }
        }
    }

    override fun deactivateUser(user: User, result: (Boolean) -> Unit) {
        user.isActive = false
        createOrUpdateUser(user, result)
    }
}