package com.example.dalsocial.model

class SocialMatches(
    private val matchesPersistence: IMatchPersistence,
    private val userManagement: IUserManagement
) : ISocialMatches {

    override fun match(
        match: Match,
        result: (Boolean) -> Unit
    ) {
        matchesPersistence.checkIfMatchExists(match) { docID ->
            if (docID == null) {
                matchesPersistence.createMatch(match) { success ->
                    //TODO Use state machine
                    result(false)
                }
            } else {
                match.approved = true
                matchesPersistence.updateMatch(docID, match) { success ->
                    //TODO Use state machine
                    result(true)
                }
            }
        }
    }

    override fun createMatch(match: Match, result: (Boolean) -> Unit) {
        matchesPersistence.createMatch(match) { success ->
            result(success)
        }
    }

    override fun getMatches(result: (List<Match>) -> Unit) {
        matchesPersistence.getMatches(userManagement.getFirebaseUserID()!!, result)
    }

    override fun getAllUsersWhoLikedMe(
        userID: String,
        result: (List<User>) -> Unit
    ) {
        matchesPersistence.getAllUsersWhoLikedMe(userID, result)
    }

    override fun filterRemoveAlreadyLikedUsers(
        users: List<User>,
        result: (ArrayList<User>) -> Unit
    ) {
        matchesPersistence.getMatchHistory(userManagement.getFirebaseUserID()!!) { matches ->
            val filteredUsers = users.filter { user ->
                matches.any { match -> match.toBeMatchedUserId != user.userID }
            }
            result(filteredUsers as ArrayList<User>)
        }
    }

}