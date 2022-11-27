package com.example.dalsocial.model

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*

@OptIn(DelicateCoroutinesApi::class)
class ChatListPersistence : IChatListPersistence {

    private val db = FirebaseFirestore.getInstance()

    private val MATCHES_COLLECTION: String = "matches"
    private val USERS_COLLECTION: String = "users"
    private val matchesRef = db.collection(MATCHES_COLLECTION)
    private val usersRef = db.collection(USERS_COLLECTION)

    val userManagement = UserManagement()
    private val currentUser: String? = userManagement.getFirebaseUserID()

    override fun getChatList(result: (List<ChatList>) -> Unit) {
        var chatlist: MutableList<ChatList> = ArrayList<ChatList>()
        try {
            GlobalScope.launch {
                matchesRef.whereEqualTo("approved", true)
                    .whereArrayContains("includedUsers", currentUser.toString()).get()
                    .addOnCompleteListener { doc ->
                        if(doc.result.documents.size>0){
                            for(i in doc.result.documents) {
                                var chatUserId: String?=null
                                if(i.getString("matchInitiatorUserId").toString()==currentUser.toString()) {
                                    chatUserId=i.getString("toBeMatchedUserId").toString()
                                } else {
                                    chatUserId=i.getString("matchInitiatorUserId").toString()
                                }
                                var connectionId: String?=i.id.toString();
                                usersRef.document(chatUserId).get().addOnSuccessListener{it ->
                                    chatlist.add(
                                        ChatList(
                                            chatUserId,
                                            it.data?.get("displayName").toString(),
                                            it.data?.get("profilePictureURL").toString(),
                                            connectionId
                                        )
                                    )
                                    if(chatlist.size==doc.result.documents.size){
                                        result(chatlist!!)
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception){
                print(e)
                result(emptyList()!!)
            }

    }
}