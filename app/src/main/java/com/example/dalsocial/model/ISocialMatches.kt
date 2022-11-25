package com.example.dalsocial.model

interface ISocialMatches {

    fun match(match: Match, result: (Boolean) -> Unit)

    fun getMatches(result: (List<Match>) -> Unit)
    fun getAllUsersWhoLikedMe(
        userID: String,
        result: (List<User>) -> Unit
    )

}