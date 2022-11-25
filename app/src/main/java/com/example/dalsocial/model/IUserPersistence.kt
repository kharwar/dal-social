package com.example.dalsocial.model

import android.net.Uri

interface IUserPersistence {

    fun getUserByID(id: String, result: (User?) -> Unit)
    fun createOrUpdateUser(user: User, result: (Boolean) -> Unit)
    fun deactivateUser(user: User, result: (Boolean) -> Unit)

    fun uploadImage(userID: String, fileName: Uri, extension: String, result: (String?) -> Unit)
    fun getAllUsers(result: (ArrayList<User>) -> Unit)
    fun getAllUsersByInterests(interests: ArrayList<String>, result: (ArrayList<User>) -> Unit)

}