package com.example.dalsocial.model

interface IUserPersistence {

    fun getUserByID(id: String, result: (User?) -> Unit)
    fun createOrUpdateUser(user: User, result: (Boolean) -> Unit)
    fun deactivateUser(user: User, result: (Boolean) -> Unit)

}