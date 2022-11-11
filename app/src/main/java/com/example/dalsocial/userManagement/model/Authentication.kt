package com.example.dalsocial.userManagement.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*

// reference for mvc: https://www.geeksforgeeks.org/mvc-model-view-controller-architecture-pattern-in-android-with-example/
class Authentication : Observable() {

    var currentUser: FirebaseUser? = null
    var auth: FirebaseAuth? = null

    init {
        currentUser = FirebaseAuth.getInstance().currentUser
        auth = FirebaseAuth.getInstance()
    }

    fun getUserID(): String? {
        if (currentUser != null) {
            return currentUser!!.uid
        }
        return null
    }

    fun getUserEmail(): String? {
        if (currentUser != null) {
            return currentUser!!.email
        }
        return null
    }

    fun getUsername(): String? {
        if (currentUser != null) {
            return currentUser!!.displayName
        }
        return null
    }

    fun loginWithEmail(email: String, password: String, function: (status: Boolean) -> Unit) {
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

    fun registerWithEmail(email: String, password: String, function: (status: Boolean) -> Unit) {
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

    fun logout() {
        if (currentUser != null) {
            auth!!.signOut()
            currentUser = null
            setChanged()
            notifyObservers()
        }
    }

    fun isLoggedIn(): Boolean {
        return currentUser != null
    }
}