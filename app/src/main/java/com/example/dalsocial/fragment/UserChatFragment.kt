package com.example.dalsocial.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.dalsocial.databinding.FragmentUsersChatBinding
import com.example.dalsocial.model.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UserChatFragment : Fragment() {
    var binding: FragmentUsersChatBinding? = null
    private val db = FirebaseFirestore.getInstance()
    private val MATCHES_COLLECTION: String = "matches"
    private val matchesRef = db.collection(MATCHES_COLLECTION)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsersChatBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val messagesPersistence: IMessagesPersistence = MessagesPersistence()
        val userManagement = UserManagement()
        val currentUser: String? = userManagement.getFirebaseUserID()
        var toUserId : String?=arguments?.getString("chatToUserId");
        var chatConnectionId: String?=arguments?.getString("chatConnectionId")
        binding?.userName?.text = arguments?.getString("userName")

//        var getMessagesQuery=matchesRef.document(chatConnectionId.toString())
//                    .collection("messages")
//                    .orderBy("timestamp", Query.Direction.ASCENDING)
//        val recyclerViewOptions=FirestoreRecyclerOptions.Builder<Messages>().setQuery(getMessagesQuery, Messages::class.java)

        binding?.sendMessageToUser?.setOnClickListener{it ->
            var fromUserId : String?=currentUser.toString();
            var message : String?=binding?.messageText?.text.toString()

            if (toUserId != null) {
                val responseMsg = GlobalScope.launch {
                    messagesPersistence.sendMessage(chatConnectionId.toString(), Messages(fromUserId,toUserId,message)) { response ->
                        if(response){
                            binding?.messageText?.text=null;
                        } else {
                            Snackbar.make(view, "Error sending message, please try again!", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
                runBlocking {
                    responseMsg.join()
                }
            }
        }
        binding?.backChatBtn?.setOnClickListener{ it ->
            Navigation.findNavController(it).navigate(UserChatFragmentDirections.actionUsersChatFragmentToChatFragment())
        }
    }
}