package com.example.dalsocial.model

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
    val linkedin: String? = "",
) {
    companion object {
        fun fromMap(map: Map<String, Any?>): User {
            return User(
                userID = map["userID"] as String?,
                firstName = map["firstName"] as String?,
                lastName = map["lastName"] as String?,
                displayName = map["displayName"] as String?,
                email = map["email"] as String?,
                interests = map["interests"] as ArrayList<String>?,
                isActive = map["isActive"] as Boolean?,
                dob = map["dob"] as Long?,
                bio = map["bio"] as String?,
                profilePictureURL = map["profilePictureURL"] as String?,
                instagram = map["instagram"] as String?,
                twitter = map["twitter"] as String?,
                facebook = map["facebook"] as String?,
                linkedin = map["linkedin"] as String?,
            )
        }
    }
}