package com.example.dalsocial.model.social

import com.example.dalsocial.model.user.User
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
    override fun hasAnyMatchByIncludedUsersID(userIDs: List<String>, result: (Boolean) -> Unit) {
        GlobalScope.launch {
            val docRef = db.collection("matches")
                .whereArrayContains("includedUsers", userIDs[0])
            val docRef2 = db.collection("matches")
                .whereArrayContains("includedUsers", userIDs[1])

            docRef.get().addOnSuccessListener { snapshot1 ->
                if (snapshot1 != null) {
                    if (snapshot1.documents.size > 0) {
                        docRef2.get().addOnSuccessListener { snapshot2 ->
                            if (snapshot2 != null) {
                                if (snapshot2.documents.size > 0) {
                                    result(true)
                                } else {
                                    result(false)
                                }
                            }
                        }.addOnFailureListener {
                        }
                    } else {
                        result(false)
                    }
                } else {
                    result(false)
                }
            }.addOnFailureListener {
            }
        }
    }

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