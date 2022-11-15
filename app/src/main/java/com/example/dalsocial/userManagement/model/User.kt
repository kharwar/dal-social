package com.example.dalsocial.userManagement.model

data class User(
    val userID: String? = "",
    val firstName: String? = "",
    val lastName: String? = "",
    val displayName: String? = "",
    val email: String? = "",
    val interests: ArrayList<String>? = ArrayList(),
    var isActive: Boolean? = false,
    val dob: Long? = 0,

    // social
    val bio: String? = "",
    val profilePictureURL: String? = "",
    val instagram: String? = "",
    val twitter: String? = "",
    val facebook: String? = "",
    val linkedInURL: String? = "",
)