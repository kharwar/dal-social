package com.example.dalsocial.model.social

data class Match(
    var approved: Boolean = false,
    var matchInitiatorUserId: String = "",
    var toBeMatchedUserId: String = "",
    val matchInitiatorUserIdLiked: Boolean = false,
    var includedUsers : ArrayList<String> = ArrayList(),
)