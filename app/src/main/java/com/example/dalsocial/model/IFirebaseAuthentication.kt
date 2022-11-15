package com.example.dalsocial.model

interface IFirebaseAuthentication {

    fun loginWithEmail(email: String, password: String, function: (status: Boolean) -> Unit)
    fun registerWithEmail(email: String, password: String, function: (status: Boolean) -> Unit)
    fun logout()
    fun isLoggedIn(): Boolean
    fun getFirebaseUserID(): String?
}