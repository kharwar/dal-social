package com.example.dalsocial.model.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dalsocial.R
import com.example.dalsocial.databinding.FragmentUsersChatBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class MessageAdapter(options: FirestoreRecyclerOptions<Messages>,
                     private val usersChatBinding: FragmentUsersChatBinding,
                     private val currentUser: String
)
    : FirestoreRecyclerAdapter<Messages, MessageAdapter.MessageViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return if (viewType == R.layout.fragment_message_to) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_message_to, parent, false)
            MessageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_message_from, parent, false)
            MessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: Messages) {
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
        usersChatBinding?.messagesRecyclerView?.layoutManager?.scrollToPosition(itemCount - 1)
    }

    inner class MessageViewHolder internal constructor(private val view: View) : RecyclerView.ViewHolder(view){
        internal fun setMessage(message: Messages){
            val tView = view.findViewById<TextView>(R.id.text_view)
            tView.text=message.message
        }
    }
}