package com.example.dalsocial.model

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
class MatchesPersistence : IMatchesPersistence {

    val TAG = "MatchesPersistence"

    val db = FirebaseFirestore.getInstance()

    val MATCHES_COLLECTION: String = "matches"
    val USERS_COLLECTION: String="users"

    private val matchesRef = db.collection(MATCHES_COLLECTION)

    private val userManagement = UserManagement()
    private val currentUser: String? = userManagement.getFirebaseUserID()

    override fun getAllMatches(result: (List<Match>) -> Unit) {

        var matchesSent: MutableList<Match> = ArrayList<Match>()
        var matchesReceived: MutableList<Match> = ArrayList<Match>()
        var matches: MutableList<Match> = ArrayList<Match>()
        var chatListData: MutableList<Match> = ArrayList<Match>()
        GlobalScope.launch {
            matchesRef.whereEqualTo("userId", currentUser).get().addOnSuccessListener { document ->
                matches = document.toObjects(Match::class.java)
                result(matches!!);
            }
                .addOnFailureListener { exception ->
                    run {
                        print(exception)
                    }
                }
        }
    }





}
