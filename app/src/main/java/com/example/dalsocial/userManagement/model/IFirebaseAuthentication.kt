package com.example.dalsocial.userManagement.model

interface IFirebaseAuthentication {

    fun loginWithEmail(email: String, password: String, function: (status: Boolean) -> Unit)
    fun registerWithEmail(email: String, password: String, function: (status: Boolean) -> Unit)
    fun logout()
    fun isLoggedIn(): Boolean
    fun getFirebaseUserID(): String?
}