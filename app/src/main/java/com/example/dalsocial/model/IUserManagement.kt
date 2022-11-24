package com.example.dalsocial.model

import android.net.Uri
import com.example.dalsocial.cards.Cards

interface IUserManagement {

    fun loginWithEmail(email: String, password: String, function: (status: Boolean) -> Unit)
    fun registerWithEmail(email: String, password: String, function: (status: Boolean) -> Unit)
    fun logout()
    fun isLoggedIn(): Boolean
    fun getFirebaseUserID(): String?
    fun getFirebaseUserEmail(): String?

    fun getUserByID(persistence: IUserPersistence, id: String, result: (User?) -> Unit)
    fun createOrUpdateUser(persistence: IUserPersistence, user: User, result: (Boolean) -> Unit)
    fun deactivateUser(persistence: IUserPersistence, user: User, result: (Boolean) -> Unit)

    fun uploadProfileImage(persistence: IUserPersistence, fileName: Uri, result: (String?) -> Unit)
    fun getAllUsers(persistence: IUserPersistence,result: (ArrayList<User>) -> Unit)
}