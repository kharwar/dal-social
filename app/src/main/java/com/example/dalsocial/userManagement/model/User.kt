package com.example.dalsocial.userManagement.model

data class User(
    val userID: String? = "",
    val displayName: String? = "",
    val email: String? = "",
    val profilePictureURL: String? = "",
    val interests: ArrayList<String>? = ArrayList(),
    var isActive: Boolean? = false,
)