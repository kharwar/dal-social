//package com.example.dalsocial.model
//
//import com.firebase.ui.firestore.FirestoreRecyclerAdapter
//import com.firebase.ui.firestore.FirestoreRecyclerOptions
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.Query
//import com.google.firebase.firestore.SetOptions
//
//class MessagesAdapter internal constructor(options: FirestoreRecyclerOptions<Messages>) : FirestoreRecyclerAdapter<Message, MessageViewHolder>(options) {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
//        return if (viewType == R.layout.item_message_to) {
//            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_to, parent, false)
//            MessageViewHolder(view)
//        } else {
//            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_from, parent, false)
//            MessageViewHolder(view)
//        }
//    }
//
//    override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: Message) {
//        holder.setMessage(model)
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return if (fromUid != getItem(position).fromUid) {
//            R.layout.item_message_to
//        } else {
//            R.layout.item_message_from
//        }
//    }
//
//    override fun onDataChanged() {
//        recycler_view.layoutManager.scrollToPosition(itemCount - 1)
//    }
//}