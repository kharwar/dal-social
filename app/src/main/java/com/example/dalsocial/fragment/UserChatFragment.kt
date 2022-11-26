package com.example.dalsocial.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.dalsocial.R
import com.example.dalsocial.databinding.FragmentUsersChatBinding
import com.example.dalsocial.model.*
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UserChatFragment : Fragment() {
    var binding: FragmentUsersChatBinding? = null
    private val db = FirebaseFirestore.getInstance()
    private var adapter: MessageAdapter? = null
    private val MATCHES_COLLECTION: String = "matches"
    private val matchesRef = db.collection(MATCHES_COLLECTION)
    val userManagement = UserManagement()
    val currentUser: String? = userManagement.getFirebaseUserID()
    var toUserId : String?=arguments?.getString("chatToUserId");
    var fromUserId : String?=currentUser.toString();
    var chatConnectionId: String?=arguments?.getString("chatConnectionId")
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

        binding?.userName?.text = arguments?.getString("userName")
        chatConnectionId =arguments?.getString("chatConnectionId")
        toUserId =arguments?.getString("chatToUserId");
        var getMessagesQuery=matchesRef.document(chatConnectionId.toString())
                    .collection("messages")
                    .orderBy("timestamp", Query.Direction.ASCENDING)
        val recyclerViewOptions=
            FirestoreRecyclerOptions.Builder<Messages>().setQuery(getMessagesQuery, Messages::class.java).build()
        adapter = MessageAdapter(recyclerViewOptions)
        binding?.messagesRecyclerView?.adapter = adapter
        binding?.sendMessageToUser?.setOnClickListener{it ->

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

    inner class messageViewHolder internal constructor(private val view: View) : RecyclerView.ViewHolder(view){
        internal fun setMessage(message: Messages){
            val tView = view.findViewById<TextView>(R.id.text_view)
            tView.text=message.message
        }
    }

    inner class MessageAdapter internal constructor(options: FirestoreRecyclerOptions<Messages>) :
        FirestoreRecyclerAdapter<Messages, messageViewHolder>(options) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): messageViewHolder {
            return if (viewType == R.layout.fragment_message_to) {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_message_to, parent, false)
                messageViewHolder(view)
            } else {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_message_from, parent, false)
                messageViewHolder(view)
            }
        }

        override fun onBindViewHolder(holder: messageViewHolder, position: Int, model: Messages) {
            holder.setMessage(model)
        }

        override fun getItemViewType(position: Int): Int {
            return if (currentUser != getItem(position).fromUserId) {
                R.layout.fragment_message_to
            } else {
                R.layout.fragment_message_from
            }
        }

        override fun onDataChanged() {
            binding?.messagesRecyclerView?.layoutManager?.scrollToPosition(itemCount - 1)
        }
    }

    override fun onStart() {
        super.onStart()

        if (adapter != null) {
            adapter!!.startListening()
        }
    }

    override fun onStop() {
        super.onStop()

        if (adapter != null) {
            adapter!!.stopListening()
        }
    }
}