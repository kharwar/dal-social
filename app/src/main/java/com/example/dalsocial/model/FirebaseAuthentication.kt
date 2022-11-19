package com.example.dalsocial.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*

// reference for mvc: https://www.geeksforgeeks.org/mvc-model-view-controller-architecture-pattern-in-android-with-example/
class FirebaseAuthentication : IFirebaseAuthentication, Observable() {

    var currentUser: FirebaseUser? = null
    var auth: FirebaseAuth? = null

    init {
        currentUser = FirebaseAuth.getInstance().currentUser
        auth = FirebaseAuth.getInstance()
    }

    override fun getFirebaseUserID(): String? {
        if (currentUser != null) {
            return currentUser!!.uid
        }
        return null
    }

    override fun getFirebaseUserEmail(): String? {
        if (currentUser != null) {
            return currentUser!!.email
        }
        return null
    }

    override fun loginWithEmail(email: String, password: String, function: (status: Boolean) -> Unit) {
        if (currentUser == null) {
            auth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        currentUser = auth!!.currentUser
                        setChanged()
                        notifyObservers()
                        function(true)
                    } else {
                        function(false)
                    }
                }
        }
    }

    override fun registerWithEmail(email: String, password: String, function: (status: Boolean) -> Unit) {
        if (currentUser == null) {
            auth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        currentUser = auth!!.currentUser
                        setChanged()
                        notifyObservers()
                        function(true)
                    } else {
                        function(false)
                    }
                }
        }
    }

    override fun logout() {
        if (currentUser != null) {
            auth!!.signOut()
            currentUser = null
            setChanged()
            notifyObservers()
        }
    }

    override fun isLoggedIn(): Boolean {
        return currentUser != null
    }
}