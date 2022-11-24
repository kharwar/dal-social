package com.example.dalsocial.model

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

@OptIn(DelicateCoroutinesApi::class)
class ChatListPersistence : IChatListPersistence {

    val TAG = "ChatListPersistence"

    val db = FirebaseFirestore.getInstance()

    val MATCHES_COLLECTION: String = "matches"
    val USERS_COLLECTION: String = "users"
    private val matchesRef = db.collection(MATCHES_COLLECTION)
    private val usersRef = db.collection(USERS_COLLECTION)

    val userManagement = UserManagement()
    val userPersistence = UserPersistence()
    private val currentUser: String? = userManagement.getFirebaseUserID()

    override fun getChatList(result: (List<ChatList>) -> Unit) {
        var chatlist: MutableList<ChatList> = ArrayList<ChatList>()
        var userIds: MutableList<String> = ArrayList<String>()
        chatlist.clear()
        userIds.clear()
        GlobalScope.launch {
            try {
                matchesRef.whereEqualTo("approved", true)
                    .whereArrayContains("includedUsers", currentUser.toString()).get()
                    .addOnCompleteListener { doc ->
                        print("SIZEEEEEEEEEEEEE     "+doc.result.documents.size)
                        Log.d("SIZE", "getChatListsize: "+doc.result.documents.size)
                        for (i in doc.result.documents) {
                            if (i.getString("toBeMatchedUserId").toString() == currentUser) {
                                userIds.add(i.getString("matchInitiatorUserId").toString())
                            } else {
                                userIds.add(i.getString("toBeMatchedUserId").toString())
                            }
                        }
                    }.await()
                print(userIds.toString())
                for (i in userIds) {
                    usersRef.document(i).get().addOnSuccessListener { doc ->
                        var documentres = doc.data
                        var profilePic = documentres?.get("profilePictureURL").toString()
                        var username = documentres?.get("displayName").toString()
                        chatlist.add(ChatList(i, username, profilePic))
                    }
                }
                result(chatlist)
            } catch (e: Exception){
                print(e)
                result(chatlist)
            }
        }
    }

    override fun getGroupChatList(result: (List<ChatList>) -> Unit) {
        var chatlist: MutableList<ChatList> = ArrayList<ChatList>()
        var userIds: MutableList<String> = ArrayList<String>()
        GlobalScope.launch {
//            chatlist.add(ChatList("DEFXYZ","DhruvitRaval","data:image/jpeg;base64,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"))
//            chatlist.add(ChatList("XYZXYZ","ParvishGajjar","data:image/jpeg;base64,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"))
            matchesRef.whereEqualTo("approved",true).whereArrayContains("includedUsers",currentUser.toString()).get().addOnCompleteListener{doc ->
                for(i in doc.result.documents){
                    if(i.getString("toBeMatchedUserId").toString()==currentUser){
                        userIds.add(i.getString("matchInitiatorUserId").toString())
                    } else {
                        userIds.add(i.getString("toBeMatchedUserId").toString())
                    }
                }
            }.await()
            for (i in userIds){
                usersRef.document(i).get().addOnSuccessListener{doc ->
                    var documentres=doc.data
                    var profilePic= documentres?.get("profilePictureURL").toString()
                    var username=documentres?.get("displayName").toString()
                    chatlist.add(ChatList(i,username,profilePic))
                }
            }
            result(chatlist)
        }
    }
}