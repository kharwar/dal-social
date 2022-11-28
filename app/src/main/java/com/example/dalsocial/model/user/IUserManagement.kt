package com.example.dalsocial.model.user

import android.content.Intent
import android.net.Uri
import com.example.dalsocial.model.states.AuthenticationState

interface IUserManagement {

    fun loginWithEmail(email: String, password: String, result: (status: AuthenticationState) -> Unit)
    fun registerWithEmail(email: String, password: String, result: (status: AuthenticationState) -> Unit)
    fun loginWithGoogle(intent: Intent, result: (status: Boolean) -> Unit)

    fun resetPassword(result: (status: Boolean) -> Unit)
    fun resetPasswordByEmail(email: String, result: (status: Boolean) -> Unit)
    fun logout()
    fun isLoggedIn(): Boolean
    fun getFirebaseUserID(): String?
    fun getFirebaseUserEmail(): String?

    fun getUserByID(persistence: IUserPersistence, id: String, result: (User?) -> Unit)
    fun createOrUpdateUser(persistence: IUserPersistence, user: User, result: (Boolean) -> Unit)
    fun deleteUser(persistence: IUserPersistence, userID: String, result: (Boolean) -> Unit)

    fun uploadProfileImage(persistence: IUserPersistence, fileName: Uri, result: (String?) -> Unit)
    fun getAllUsers(persistence: IUserPersistence, result: (ArrayList<User>) -> Unit)
    fun getAllUsersByInterests(
        persistence: IUserPersistence,
        interests: ArrayList<String>,
        result: (ArrayList<User>) -> Unit
    )
}