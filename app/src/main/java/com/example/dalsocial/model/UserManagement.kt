package com.example.dalsocial.model

import android.content.Intent
import android.net.Uri
import com.example.dalsocial.model.states.AuthenticationState
import com.example.dalsocial.model.states.AuthenticationSuccessState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser

// reference for mvc: https://www.geeksforgeeks.org/mvc-model-view-controller-architecture-pattern-in-android-with-example/
class UserManagement : IUserManagement {

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

    override fun getUserByID(persistence: IUserPersistence, id: String, result: (User?) -> Unit) {
        persistence.getUserByID(id, result)
    }

    override fun createOrUpdateUser(
        persistence: IUserPersistence,
        user: User,
        result: (Boolean) -> Unit
    ) {
        persistence.createOrUpdateUser(user, result)
    }

    override fun deleteUser(
        persistence: IUserPersistence,
        userID: String,
        result: (Boolean) -> Unit
    ) {
        persistence.deleteUser(userID, result)
    }

    override fun uploadProfileImage(
        persistence: IUserPersistence,
        uri: Uri,
        result: (String?) -> Unit
    ) {
        persistence.uploadImage(getFirebaseUserID()!!, uri, getFileExtension(uri)!!, result)
    }

    override fun getAllUsers(persistence: IUserPersistence, result: (ArrayList<User>) -> Unit) {
        persistence.getAllUsers(result)
    }

    override fun getAllUsersByInterests(
        persistence: IUserPersistence,
        interests: ArrayList<String>,
        result: (ArrayList<User>) -> Unit
    ) {
        persistence.getAllUsersByInterests(interests, result)
    }

    override fun loginWithEmail(
        email: String,
        password: String,
        result: (status: AuthenticationState) -> Unit
    ) {
        if (currentUser == null) {
            auth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        currentUser = auth!!.currentUser
                        result(AuthenticationSuccessState())
                    } else {
                        if (task.exception is FirebaseAuthException) {
                            result(AuthenticationState((task.exception as FirebaseAuthException).errorCode))
                        }
                    }
                }
        }
    }

    override fun registerWithEmail(
        email: String,
        password: String,
        result: (status: AuthenticationState) -> Unit
    ) {
        if (currentUser == null) {
            auth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        currentUser = auth!!.currentUser
                        result(AuthenticationSuccessState())
                    } else {
                        if (task.exception is FirebaseAuthException) {
                            result(AuthenticationState((task.exception as FirebaseAuthException).errorCode))
                        }
                    }
                }.addOnFailureListener { exception ->
                    result(AuthenticationState(exception.message!!))
                }
        }
    }

    override fun loginWithGoogle(intent: Intent, result: (status: Boolean) -> Unit) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
        val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
        if (account != null) {
            result(true)
        }
        result(false)
    }


    override fun resetPassword(function: (status: Boolean) -> Unit) {
        // reference: https://firebase.google.com/docs/auth/android/manage-users
        if (currentUser != null) {
            currentUser?.sendEmailVerification()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    function(true)
                } else {
                    function(false)
                }
            }
        }
        function(false)
    }

    override fun resetPasswordByEmail(email: String, function: (status: Boolean) -> Unit) {
        if (auth != null) {}
        function(false)
        auth?.sendPasswordResetEmail(email)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                function(true)
            } else {
                function(false)
            }
        }
    }

    override fun logout() {
        if (currentUser != null) {
            auth!!.signOut()
            currentUser = null
        }
    }

    override fun isLoggedIn(): Boolean {
        return currentUser != null
    }

    private fun getFileExtension(uri: Uri): String? {
        return uri.toString().substring(uri.toString().lastIndexOf("/") + 1);
    }
}