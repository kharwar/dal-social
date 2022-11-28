package com.example.dalsocial.model

interface IMatchPersistence {

    fun createMatch(match: Match, result: (Boolean) -> Unit)
    fun updateMatch(matchId: String, match: Match, result: (Boolean) -> Unit)
    fun checkIfMatchExists(match: Match, result: (String?) -> Unit)

    fun getMatches(userID: String, result: (List<Match>) -> Unit)
    fun getAllUsersWhoLikedMe(userID: String, result: (List<User>) -> Unit)
    fun hasAnyMatchByIncludedUsersID(userIDs: List<String>, result: (Boolean) -> Unit)

    fun getMatchHistory(userID: String, result: (List<Match>) -> Unit)

}