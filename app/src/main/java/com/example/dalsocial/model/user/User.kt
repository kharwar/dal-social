package com.example.dalsocial.model.user

data class User(
    var userID: String? = "",
    var firstName: String? = "",
    var lastName: String? = "",
    var displayName: String? = "",
    var email: String? = "",
    var interests: ArrayList<String>? = ArrayList(),
    var isActive: Boolean? = false,
    var dob: Long? = 0,

    // social
    var bio: String? = "",
    var profilePictureURL: String? = "",
    var instagram: String? = "",
    var twitter: String? = "",
    var facebook: String? = "",
    var linkedin: String? = "",
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