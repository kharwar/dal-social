package com.example.dalsocial.model

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
class MatchPersistence : IMatchPersistence {

    private val db = FirebaseFirestore.getInstance()

    override fun createMatch(match: Match, result: (Boolean) -> Unit) {
        GlobalScope.launch {
            db.collection("matches")
                .add(match)
                .addOnSuccessListener {
                    result(true)
                }
                .addOnFailureListener {
                    result(false)
                }
        }
    }

    override fun updateMatch(matchId: String, match: Match, result: (Boolean) -> Unit) {
        GlobalScope.launch {
            db.collection("matches")
                .document(matchId)
                .set(match)
                .addOnSuccessListener {
                    result(true)
                }
                .addOnFailureListener {
                    result(false)
                }
        }
    }

    override fun checkIfMatchExists(match: Match, result: (String?) -> Unit) {
        GlobalScope.launch {

            var docID: String? = null;

            val docRef = db.collection("matches")
                .whereEqualTo("matchInitiatorUserId", match.matchInitiatorUserId)
                .whereEqualTo("toBeMatchedUserId", match.toBeMatchedUserId)
            docRef.get().addOnSuccessListener { snapshot ->
                if (snapshot != null && snapshot.documents.size > 0) {
                    docID = snapshot.documents[0].id
                    result(docID)
                } else {
                    val docRef2 = db.collection("matches")
                        .whereEqualTo("matchInitiatorUserId", match.toBeMatchedUserId)
                        .whereEqualTo("toBeMatchedUserId", match.matchInitiatorUserId)
                    docRef2.get().addOnSuccessListener { snapshot ->
                        if (snapshot != null && snapshot.documents.size > 0) {
                            docID = snapshot.documents[0].id
                            result(docID)
                        } else {
                            result(null)
                        }
                    }.addOnFailureListener {
                    }
                }
            }.addOnFailureListener {
                //TODO: Handle exceptions
            }
        }
    }

    override fun getMatches(userID: String, result: (List<Match>) -> Unit) {
        GlobalScope.launch {
            val docRef = db.collection("matches")
                .whereArrayContains("includedUsers", userID)
            docRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    val matches = document.toObjects(Match::class.java)
                    result(matches)
                }
            }.addOnFailureListener {
            }
        }

    }

    override fun getAllUsersWhoLikedMe(userID: String, result: (List<User>) -> Unit) {}

    override fun getMatchHistory(userID: String, result: (List<Match>) -> Unit) {
        GlobalScope.launch {
            val docRef = db.collection("matches")
                .whereEqualTo("matchInitiatorUserId", userID)
            docRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    val matches = document.toObjects(Match::class.java)
                    result(matches)
                }
            }.addOnFailureListener {
            }
        }
    }

}